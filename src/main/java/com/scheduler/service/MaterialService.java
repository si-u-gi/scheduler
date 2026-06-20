package com.scheduler.service;

import com.sidaesaeng.dto.MaterialResponse;
import com.sidaesaeng.dto.request.MaterialCreateRequest;
import com.sidaesaeng.entity.Crew;
import com.sidaesaeng.entity.Material;
import com.sidaesaeng.entity.Tag;
import com.sidaesaeng.entity.User;
import com.sidaesaeng.exception.ResourceNotFoundException;
import com.sidaesaeng.repository.CrewRepository;
import com.sidaesaeng.repository.MaterialRepository;
import com.sidaesaeng.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final CrewRepository crewRepository;
    private final TagRepository tagRepository;
    private final UserService userService;
    private final CrewService crewService;

    @Transactional
    public MaterialResponse createMaterial(MaterialCreateRequest request, Long userId) {
        Crew crew = crewRepository.findById(request.getCrewId())
                .orElseThrow(() -> new ResourceNotFoundException("크루를 찾을 수 없습니다"));

        crewService.validateCrewMember(request.getCrewId(), userId);
        User user = userService.getUserEntityById(userId);

        Set<Tag> tags = resolveTags(request.getTags());

        Material material = Material.builder()
                .crew(crew)
                .title(request.getTitle())
                .description(request.getDescription())
                .creator(user)
                .tags(tags)
                .build();

        material = materialRepository.save(material);
        return MaterialResponse.from(material);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMaterialsByCrew(Long crewId, Long userId) {
        crewService.validateCrewMember(crewId, userId);
        return materialRepository.findAllByCrewIdOrderByCreatedAtDesc(crewId).stream()
                .map(MaterialResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMaterialsByCrewAndTag(Long crewId, String tagName, Long userId) {
        crewService.validateCrewMember(crewId, userId);
        return materialRepository.findByCrewIdAndTagName(crewId, tagName).stream()
                .map(MaterialResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MaterialResponse getMaterialById(Long materialId, Long userId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("자료를 찾을 수 없습니다"));

        crewService.validateCrewMember(material.getCrew().getId(), userId);
        return MaterialResponse.from(material);
    }

    @Transactional
    public void deleteMaterial(Long materialId, Long userId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("자료를 찾을 수 없습니다"));

        crewService.validateCrewMember(material.getCrew().getId(), userId);
        materialRepository.delete(material);
    }

    @Transactional
    public MaterialResponse incrementDownloadCount(Long materialId, Long userId) {
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("자료를 찾을 수 없습니다"));

        crewService.validateCrewMember(material.getCrew().getId(), userId);

        material.setDownloadCount(material.getDownloadCount() + 1);
        material = materialRepository.save(material);
        return MaterialResponse.from(material);
    }

    private Set<Tag> resolveTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return new HashSet<>();
        }

        Set<Tag> tags = new HashSet<>();
        for (String name : tagNames) {
            Tag tag = tagRepository.findByName(name)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(name).build()));
            tags.add(tag);
        }
        return tags;
    }
}
