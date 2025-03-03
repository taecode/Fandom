package com.example.NewJeans.repository;

import com.example.NewJeans.entity.Board;
import com.example.NewJeans.entity.Idol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {


    @Query("select t from Board t where t.idol=:idol")
    List<Board> findByIdolId(@Param("idol") Long idolId);

//    @Modifying
//    @Query("update Board b set b.boardCnt=b.boardCnt+1 where b.boardid=:boardid")
//    int updateBoardCnt(Long boardId);

}
