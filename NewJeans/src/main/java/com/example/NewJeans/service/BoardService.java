package com.example.NewJeans.service;


import com.example.NewJeans.dto.request.CreateBoardRequestDTO;
import com.example.NewJeans.dto.request.ModifyBoardRequestDTO;
import com.example.NewJeans.dto.response.DetailBoardResponseDTO;
import com.example.NewJeans.dto.response.ListBoardResponseDTO;
import com.example.NewJeans.entity.Board;
import com.example.NewJeans.entity.Idol;
import com.example.NewJeans.repository.BoardRepository;
import com.example.NewJeans.repository.IdolRepository;
import com.example.NewJeans.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {


    private final BoardRepository boardRepository;
    private final IdolRepository idolRepository;

    //게시판 목록 조회  페이징 처리 필요
    @Transactional
    public ListBoardResponseDTO retrieve(Long idolId, Pageable pageable){ //Long memId,,int page, int size, String sort

        Page<Board> listBoards = boardRepository.findByIdolId(idolId,pageable);

       // List<Board> boardList = boardRepository.findByIdolContaining(idolId, pageable);

        //페이징처리

        //Page<Board> pageBoards = boardRepository.findAll(PageRequest.of(page - 1, size,Sort.by(sort).descending()));

        //List<Board> listBoards= pageBoards.getContent();

        List<DetailBoardResponseDTO> dtoList = listBoards.stream()
                .map(DetailBoardResponseDTO::new)
                .collect(Collectors.toList());


        return ListBoardResponseDTO.builder()
                .boards(dtoList)
                .build();

    }

    //게시물 등록
    public Long create(final CreateBoardRequestDTO createRequestDTO, final Long idolId )
        throws RuntimeException
    {
        Idol idol=new Idol();
        idol.setIdolID(idolId);

        Board board=createRequestDTO.toEntity();
        board.setIdolID(idol);
        board.setIdol(idolId);
        board.setMemNickName(board.getMemNickName()); //작성자 닉네임
        boardRepository.save(board);
        log.info("게시물이 등록되었습니다. 내용:{} 파일:{}",createRequestDTO.getBoardContent(),createRequestDTO.getBoardFile());

        return idolId;

    }

    //게시물 삭제
    public void delete(final Long boardId, final Long idolId){ //final Long memId,
        try {
            boardRepository.deleteById(boardId);
        } catch (Exception e) {
            log.error("board-id가 존재하지 않아 삭제에 실패했습니다.-boardId:{}, err: {}",
                    boardId,e.getMessage());
            throw new RuntimeException("boardId가 존재하지 않아 삭제에 실패했습니다.");
        }

    }
//
//
    //게시물 수정
    public void update(
              final Long boardId,final Long idolId, final ModifyBoardRequestDTO requestDTO){ //final Long memId,

        Optional<Board> targetEntity=boardRepository.findById(boardId);

        targetEntity.ifPresent(entity->{
            entity.setBoardContent(requestDTO.getBoardContent());
            entity.setBoardFile(requestDTO.getBoardFile());
            entity.setMemNickName(requestDTO.getMemNickName());
            boardRepository.save(entity);
        });



    }


    //게시물 조회수


}















