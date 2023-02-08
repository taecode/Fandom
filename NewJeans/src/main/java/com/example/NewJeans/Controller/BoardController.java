package com.example.NewJeans.Controller;


import com.example.NewJeans.dto.request.BoardCreateRequestDTO;
import com.example.NewJeans.dto.request.BoardModifyRequestDTO;
import com.example.NewJeans.dto.response.BoardDetailResponseDTO;
import com.example.NewJeans.dto.response.BoardListResponseDTO;
import com.example.NewJeans.entity.Idol;
import com.example.NewJeans.repository.IdolRepository;
import com.example.NewJeans.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;



    //게시글 목록 요청  idolID로 idol별 게시물 페이지 요청
//    @GetMapping("/{idol-id}")
//    public ResponseEntity<?> retrieveBoardList(
//            //@AuthenticationPrincipal Long memId,
//            Model model,
//            @PathVariable("idol-id") Long idolId,
//            @RequestParam(name = "page", required = false, defaultValue = "1")int page,
//            @RequestParam(name = "size", required = false, defaultValue = "10")int size,
//            @RequestParam(name = "sort", required = false, defaultValue = "idolID")String sort
//    )
//    {
//        log.info("/board/{} Get request!",idolId);
//
//        try {
//            BoardListResponseDTO responseDTO = boardService.retrieve(idolId,page,size,sort); //memId
//            return ResponseEntity.ok().body(responseDTO);
//
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError()
//                    .body(BoardListResponseDTO.builder().error(e.getMessage()));
//
//        }
//
//    }
    @GetMapping
    public String retrieveBoardList(
            Model model,
            //인증된 회원
            //@AuthenticationPrincipal Long memId,
            @PathVariable("idol-id") Long idolId,
            @RequestParam(name = "page", required = false, defaultValue = "1")int page,
            @RequestParam(name = "size", required = false, defaultValue = "10")int size,
            @RequestParam(name = "sort", required = false, defaultValue = "idol")String sort

    )
    {
            // log.info("/board/{} Get request!",idolId);

            BoardListResponseDTO BoardListResponseDTO = boardService.retrieve(idolId,page,size,sort);
            //BoardListResponseDTO BoardListResponseDTO = boardService.retrieve(idolId); //memId,page,size,sort
            model.addAttribute("BoardListResponseDTO",BoardListResponseDTO);
            return "list";

    }


    //게시글 등록 요청   파일 업로드 추가 필요
    //@PostMapping("/{idol-id}")
    @PostMapping("/{idol-id}")
    public String createBoard(
            Model model,
            //@AuthenticationPrincipal Long memId,
            //아이돌 번호도 넘어와야함
            @PathVariable("idol-id") Long idolId,
            @Validated @RequestBody BoardCreateRequestDTO requestDTO,
            BindingResult result

    )
    {

        if (result.hasErrors()) {
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            model.addAttribute("error","createBoard 에러");
//            return ResponseEntity
//                    .badRequest()
//                    .body(result.getFieldError());
            return null; //페이지 만들어서 보내
        }

        try {
            BoardDetailResponseDTO boardDetailResponseDTO = boardService.create(requestDTO,idolId); //memId,idolId
            model.addAttribute("boardDetailResponseDTO",boardDetailResponseDTO);
            return "list";

//            return ResponseEntity
//                    .ok()
//                    .body(boardDetailResponseDTO);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            model.addAttribute("error","createBoard 에러");
            return null; //페이지 만들어서 보내
//            return ResponseEntity
//                    .internalServerError()
//                    .body(BoardListResponseDTO.builder().error(e.getMessage()));
        }

    }


    //게시글 삭제 요청  작성자 OR 관리자일 경우만 삭제
    @DeleteMapping("/{idol-id}/{board-id}")  //헷갈린다
    public ResponseEntity<?> deleteBoard(
            //@AuthenticationPrincipal Long memId,
            @PathVariable("board-id") Long boardId,
            @PathVariable("idol-id") Long idolId
     )
    {
        log.info("/board/{} DELETE request!", boardId);

        if (boardId == null) {
            return ResponseEntity
                    .badRequest()
                    .body(BoardListResponseDTO.builder().error("boardID를 전달해주세요"));
        }

        try {
            BoardListResponseDTO responseDTO = boardService.delete(boardId,idolId); //memId
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(BoardListResponseDTO.builder().error(e.getMessage()));
        }


    }

    //
//
//    //게시글 수정 요청
    @RequestMapping(
            value = "/{idol-id}/{board-id}"
            , method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> updateBoard(
            //@AuthenticationPrincipal Long memId,
            @PathVariable("board-id") Long boardId,
            @PathVariable("idol-id") Long idolId,
            @Validated @RequestBody BoardModifyRequestDTO requestDTO, BindingResult result, HttpServletRequest request
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldError());
        }
        log.info("/board/{} {} request", boardId, request.getMethod());
        log.info("modifying dto: {}", requestDTO);

        try {
            BoardListResponseDTO responseDTO = boardService.update(boardId,idolId, requestDTO); //memId
            return ResponseEntity
                    .ok()
                    .body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(BoardListResponseDTO.builder().error(e.getMessage()));
        }

    }

    //게시글 조회수

}






















