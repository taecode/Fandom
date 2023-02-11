package com.example.NewJeans.service;

import com.example.NewJeans.dto.request.CreateIdolRequestDTO;
import com.example.NewJeans.dto.request.ModifyIdolRequestDTO;
import com.example.NewJeans.dto.response.DetailIdolImgResponseDTO;
import com.example.NewJeans.dto.response.DetailIdolResponseDTO;
import com.example.NewJeans.dto.response.ListIdolResponseDTO;
import com.example.NewJeans.entity.Idol;
import com.example.NewJeans.repository.IdolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdolService {
    private final IdolRepository idolRepository;

    public DetailIdolResponseDTO create(CreateIdolRequestDTO createIdolRequestDTO){
        Idol idol = createIdolRequestDTO.toEntity();
        Idol savedIdol = idolRepository.save(idol);

        log.info("아이돌 save 완료 : {}",savedIdol);
        return new DetailIdolResponseDTO(savedIdol);
    }

    public ListIdolResponseDTO findIdols(int page, int size, String sort){
        Page<Idol> foundIdols = idolRepository.findAll(PageRequest.of(page - 1, size, Sort.by(sort).descending()));
        List<Idol> listIdols = foundIdols.getContent();

        List<DetailIdolResponseDTO> responseDTOS =
                listIdols.stream()
                        .map(DetailIdolResponseDTO::new)
                        .collect(Collectors.toList());
        return ListIdolResponseDTO.builder()
                .idols(responseDTOS)
                .size(foundIdols.getSize())
                .page(page)
                .totalElements(foundIdols.getTotalElements())
                .totalPages(foundIdols.getTotalPages())
                .hasNext(foundIdols.hasNext())
                .hasPrevious(foundIdols.hasPrevious())
                .build();
    }

    public DetailIdolResponseDTO findIdol(Long idolId){
        Idol verifiedIdol = findVerfiedIdol(idolId);
        return new DetailIdolResponseDTO(verifiedIdol);
    }

    public DetailIdolResponseDTO updateIdol(Long idolId, ModifyIdolRequestDTO modifyIdolRequestDTO){
        Idol verifiedIdol = findVerfiedIdol(idolId);
        Optional.ofNullable(modifyIdolRequestDTO.getIdolName())
                .ifPresent(verifiedIdol::setIdolName);
        Optional.ofNullable(modifyIdolRequestDTO.getIdolMainImg())
                .ifPresent(verifiedIdol::setIdolMainImg);

        Idol savedIdol = idolRepository.save(verifiedIdol);
        return new DetailIdolResponseDTO(savedIdol);
    }

    public DetailIdolResponseDTO removeIdol(Long idolId){
        Idol verifiedIdol = findVerfiedIdol(idolId);
        idolRepository.delete(verifiedIdol);
        log.info("아이돌 삭제 : {}", verifiedIdol);
        return new DetailIdolResponseDTO(verifiedIdol);
    }

    public Idol findVerfiedIdol(Long idolId){
        Optional<Idol> optionalIdol = idolRepository.findById(idolId);
        return optionalIdol.orElseThrow(() -> new RuntimeException("아이돌이 존재하지 않습니다."));
    }
}