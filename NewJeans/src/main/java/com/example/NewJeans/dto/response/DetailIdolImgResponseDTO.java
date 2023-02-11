package com.example.NewJeans.dto.response;

import com.example.NewJeans.entity.Idol;
import com.example.NewJeans.entity.IdolImg;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class DetailIdolImgResponseDTO {
    private Long imgId;
    private String imgPath;
    private String msType;
    private String idolName;
    @JsonFormat(pattern = "yyyy년 MM월 dd일 a hh시 mm분 ss초")
    private LocalDateTime imgDate;

    public DetailIdolImgResponseDTO(IdolImg entity){
        this.imgId = entity.getImgId();
        this.imgPath = entity.getImgPath();
        this.msType = entity.getMsType();
        this.idolName = entity.getIdolName();
        this.imgDate = entity.getImgDate();
    }
}