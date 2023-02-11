package com.example.NewJeans.controller;


import com.example.NewJeans.dto.request.CreateBoardRequestDTO;
import com.example.NewJeans.dto.request.ModifyBoardRequestDTO;
import com.example.NewJeans.dto.response.DetailBoardResponseDTO;
import com.example.NewJeans.dto.response.ListBoardResponseDTO;
import com.example.NewJeans.entity.Idol;
import com.example.NewJeans.repository.IdolRepository;
import com.example.NewJeans.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;



    //게시글 조회
    @GetMapping("/{idol-id}")   //아이돌 번호에 따라 페이징
    public String retrieveBoardList(
            Model model,
            @AuthenticationPrincipal Long memId,//인증된 회원
            @PathVariable("idol-id") Long idolId,
            @PageableDefault(size = 10, sort = "boardID", direction = Sort.Direction.DESC) Pageable pageable

    )
    {

        log.info("/board/{} Get request!",idolId);
        ListBoardResponseDTO listBoardResponseDTO = boardService.retrieve(idolId,pageable);
        model.addAttribute("ListBoardResponseDTO", listBoardResponseDTO);
        model.addAttribute("IdolId",idolId);
        return "board/boardList";

    }

    //게시글 작성 폼으로
    @GetMapping("/{idol-id}/boardWrite")
    public String boardWrite() {return "board/boardWrite";}


    //게시글 등록 요청    (파일 업로드 추가 필요)
    @PostMapping("/{idol-id}")
    public String createBoard(
            Model model,
            //@AuthenticationPrincipal Long memId,
            @PathVariable("idol-id") Long idolId,
            @Validated @RequestBody CreateBoardRequestDTO requestDTO,
            RedirectAttributes redirectAttributes,
            BindingResult result

    )
    {
        if (result.hasErrors()) {
            log.warn("DTO 검증 에러 발생: {}", result.getFieldError());
            model.addAttribute("error","createBoard 에러");
            return null; //에러페이지

        }

        try {
            boardService.create(requestDTO,idolId);
            Long idol=boardService.create(requestDTO,idolId);
            redirectAttributes.addAttribute("idol",idol);
            return "redirect:/{idol}"; //게시판 페이지로 리다이렉트

        } catch (RuntimeException e) {
            log.error(e.getMessage());
            model.addAttribute("error","createBoard 에러");

            return null; ////에러페이지
        }

    }


    //게시글 삭제 요청    (작성자 OR 관리자일 경우만 삭제)
    @DeleteMapping("/{idol-id}/{board-id}")
    public String deleteBoard(
            Model model,
            //@AuthenticationPrincipal Long memId,
            @PathVariable("board-id") Long boardId,
            @PathVariable("idol-id") Long idolId
     )
    {
        log.info("/board/{} DELETE request!", boardId);

        if (boardId == null) {
            return null; //에러페이지
        }

        try {
            boardService.delete(boardId,idolId); //memId
            return "redirect:/"; //게시판 페이지

        } catch (Exception e) {
            log.warn("게시글 삭제 에러 : {}", e.getMessage());
            model.addAttribute("error","deleteBoard 에러");
            return null; //에러페이지

        }


    }

    //게시글 수정 요청   (작성자만 수정 가능)
    @RequestMapping(
            value = "/{idol-id}/{board-id}"
            , method = {RequestMethod.PUT, RequestMethod.PATCH})
    public String updateBoard(
            Model model,
            //@AuthenticationPrincipal Long memId,
            @PathVariable("board-id") Long boardId,
            @PathVariable("idol-id") Long idolId,
            @Validated @RequestBody ModifyBoardRequestDTO requestDTO, BindingResult result, HttpServletRequest request
    ) {
        if (result.hasErrors()) {
            return null; //에러페이지
        }
        log.info("/board/{} {} request", boardId, request.getMethod());
        log.info("modifying dto: {}", requestDTO);

        try {
            boardService.update(boardId,idolId, requestDTO);
            return "redirect:/"; //게시판 페이지

        } catch (Exception e) {
            log.warn("게시글 수정 에러 : {}", e.getMessage());
            model.addAttribute("error","updateBoard 에러");
            return null; //에러페이지

        }

    }


}






















