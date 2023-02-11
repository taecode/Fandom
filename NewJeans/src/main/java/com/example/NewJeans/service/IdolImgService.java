package com.example.NewJeans.service;

import com.example.NewJeans.dto.request.CreateIdolImgRequestDTO;
import com.example.NewJeans.dto.request.ModifyIdolImgRequestDTO;
import com.example.NewJeans.dto.response.DetailIdolImgResponseDTO;
import com.example.NewJeans.dto.response.ListIdolImgResponseDTO;
import com.example.NewJeans.entity.Idol;
import com.example.NewJeans.entity.IdolImg;
import com.example.NewJeans.repository.IdolImgRepository;
import com.example.NewJeans.repository.IdolRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdolImgService {

    private final IdolImgRepository idolImgRepository;
    private final IdolRepository idolRepository;

    public DetailIdolImgResponseDTO create(CreateIdolImgRequestDTO createIdolImgRequestDTO){
        // RequestDTO -> 엔티티
        IdolImg idolImg = createIdolImgRequestDTO.toEntity();
        Optional<Idol> foundIdol = idolRepository.findByIdolName(idolImg.getIdolName());
        Idol idol = foundIdol.orElseThrow(() -> new RuntimeException("아이돌이 존재하지 않습니다."));
        idolImg.setIdol(idol);
        idolImgRepository.save(idolImg);
        log.info("아이돌 이미지 업로드 완료 : {}",idolImg.getImgId());

        return new DetailIdolImgResponseDTO(idolImg);
    }

    // 상세 아이돌 이미지
    public DetailIdolImgResponseDTO findIdolImg(Long imageId){
        IdolImg idolImg = findVerifiedIdolImg(imageId);
        return new DetailIdolImgResponseDTO(idolImg);
    }

    // 리스트 아이돌 이미지
    public ListIdolImgResponseDTO findIdolImgs(int page, int size, String sort){
        // 페이징처리 + 목록 불러오기
        Page<IdolImg> pageImgs = idolImgRepository.findAll(PageRequest.of(page - 1, size, Sort.by(sort).descending()));

        // responseDTO 리스트로 변환
        List<IdolImg> listImgs = pageImgs.getContent();
        List<DetailIdolImgResponseDTO> listImgResponseDTOs = listImgs
                .stream().
                map(DetailIdolImgResponseDTO::new)
                .collect(Collectors.toList());

        return ListIdolImgResponseDTO.builder()
                .idolImages(listImgResponseDTOs)
                .size(pageImgs.getSize())
                .page(page)
                .totalElements(pageImgs.getTotalElements())
                .totalPages(pageImgs.getTotalPages())
                .hasNext(pageImgs.hasNext())
                .hasPrevious(pageImgs.hasPrevious())
                .build();
    }

    public DetailIdolImgResponseDTO update(Long imageId, ModifyIdolImgRequestDTO modifyIdolImgRequestDTO){
        IdolImg idolImg = findVerifiedIdolImg(imageId);
        Optional.ofNullable(modifyIdolImgRequestDTO.getIdolName())
                .ifPresent(idolImg::setIdolName);
        Optional.ofNullable(modifyIdolImgRequestDTO.getImgPath())
                .ifPresent(idolImg::setImgPath);
        Optional.ofNullable(modifyIdolImgRequestDTO.getMsType())
                .ifPresent(idolImg::setMsType);
        idolImg.setImgDate(LocalDateTime.now());

        IdolImg savedIdolImg = idolImgRepository.save(idolImg);
        return new DetailIdolImgResponseDTO(savedIdolImg);
    }

    public DetailIdolImgResponseDTO remove(Long imageId){
        IdolImg idolImg = findVerifiedIdolImg(imageId);
        idolImgRepository.delete(idolImg);
        log.info("{}번 이미지 삭제 완료", imageId);
        return new DetailIdolImgResponseDTO(idolImg);
    }

    public IdolImg findVerifiedIdolImg(Long imageId){
        Optional<IdolImg> optionalIdolImg = idolImgRepository.findById(imageId);
        return optionalIdolImg.orElseThrow(() -> new RuntimeException("이미지가 존재하지 않습니다."));
    }
}