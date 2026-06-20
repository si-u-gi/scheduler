package com.scheduler.service;

import com.sidaesaeng.dto.CrewMemberResponse;
import com.sidaesaeng.dto.CrewResponse;
import com.sidaesaeng.dto.request.CrewCreateRequest;
import com.sidaesaeng.dto.request.CrewJoinRequest;
import com.sidaesaeng.entity.Crew;
import com.sidaesaeng.entity.CrewMember;
import com.sidaesaeng.entity.User;
import com.sidaesaeng.exception.BadRequestException;
import com.sidaesaeng.exception.ForbiddenException;
import com.sidaesaeng.exception.ResourceNotFoundException;
import com.sidaesaeng.repository.CrewMemberRepository;
import com.sidaesaeng.repository.CrewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final UserService userService;

    private static final String INVITE_CODE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public CrewResponse createCrew(CrewCreateRequest request, Long userId) {
        User user = userService.getUserEntityById(userId);

        Crew crew = Crew.builder()
                .name(request.getName())
                .description(request.getDescription())
                .inviteCode(generateInviteCode())
                .creator(user)
                .build();

        crew = crewRepository.save(crew);

        // Add creator as leader
        CrewMember leader = CrewMember.builder()
                .crew(crew)
                .user(user)
                .role(CrewMember.CrewRole.LEADER)
                .build();
        crewMemberRepository.save(leader);

        return toCrewResponse(crew);
    }

    @Transactional(readOnly = true)
    public List<CrewResponse> getMyCrews(Long userId) {
        return crewRepository.findAllByUserId(userId).stream()
                .map(this::toCrewResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CrewResponse getCrewById(Long crewId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new ResourceNotFoundException("크루를 찾을 수 없습니다"));
        return toCrewResponse(crew);
    }

    @Transactional
    public CrewResponse joinCrew(CrewJoinRequest request, Long userId) {
        Crew crew = crewRepository.findByInviteCode(request.getInviteCode())
                .orElseThrow(() -> new BadRequestException("유효하지 않은 초대코드입니다"));

        User user = userService.getUserEntityById(userId);

        if (crewMemberRepository.existsByCrewIdAndUserId(crew.getId(), userId)) {
            throw new BadRequestException("이미 가입된 크루입니다");
        }

        CrewMember member = CrewMember.builder()
                .crew(crew)
                .user(user)
                .role(CrewMember.CrewRole.MEMBER)
                .build();
        crewMemberRepository.save(member);

        return toCrewResponse(crew);
    }

    @Transactional(readOnly = true)
    public List<CrewMemberResponse> getCrewMembers(Long crewId) {
        validateCrewExists(crewId);
        return crewMemberRepository.findAllByCrewIdWithUser(crewId).stream()
                .map(CrewMemberResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCrew(Long crewId, Long userId) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new ResourceNotFoundException("크루를 찾을 수 없습니다"));

        CrewMember leader = crewMemberRepository.findByCrewIdAndRole(crewId, CrewMember.CrewRole.LEADER)
                .orElseThrow(() -> new ForbiddenException("크루장이 존재하지 않습니다"));

        if (!leader.getUser().getId().equals(userId)) {
            throw new ForbiddenException("크루장만 크루를 삭제할 수 있습니다");
        }

        crewMemberRepository.deleteAll(crewMemberRepository.findByCrewId(crewId));
        crewRepository.delete(crew);
    }

    @Transactional(readOnly = true)
    public void validateCrewMember(Long crewId, Long userId) {
        if (!crewMemberRepository.existsByCrewIdAndUserId(crewId, userId)) {
            throw new ForbiddenException("해당 크루의 회원이 아닙니다");
        }
    }

    private void validateCrewExists(Long crewId) {
        if (!crewRepository.existsById(crewId)) {
            throw new ResourceNotFoundException("크루를 찾을 수 없습니다");
        }
    }

    private String generateInviteCode() {
        String code;
        do {
            code = random.ints(0, INVITE_CODE_CHARACTERS.length())
                    .limit(6)
                    .mapToObj(INVITE_CODE_CHARACTERS::charAt)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        } while (crewRepository.existsByInviteCode(code));
        return code;
    }

    private CrewResponse toCrewResponse(Crew crew) {
        int memberCount = crewMemberRepository.countByCrewId(crew.getId());
        CrewMember leader = crewMemberRepository.findByCrewIdAndRole(crew.getId(), CrewMember.CrewRole.LEADER)
                .orElse(null);

        return CrewResponse.builder()
                .id(crew.getId())
                .name(crew.getName())
                .description(crew.getDescription())
                .inviteCode(crew.getInviteCode())
                .createdAt(crew.getCreatedAt())
                .memberCount(memberCount)
                .leader(leader != null ? CrewMemberResponse.from(leader) : null)
                .build();
    }
}
