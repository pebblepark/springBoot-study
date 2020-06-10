package com.example.springboot.board.mapper;

import com.example.springboot.board.dto.BoardDto;
import com.example.springboot.board.dto.BoardFileDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDto> selectBoardList() throws Exception;

    void insertBoard(BoardDto board) throws Exception;

    BoardDto selectBoardDetail(int boardIdx) throws Exception;

    void updateHitCount(int boardIdx) throws Exception;

    void updateBoard(BoardDto board) throws Exception;

    void deleteBoard(int boardIdx) throws Exception;

    void insertBoardFileList(List<BoardFileDto> list) throws Exception;

    List<BoardFileDto> selectBoardFileList(int boardIdx) throws Exception;

    BoardFileDto selectBoardFileInformation(@Param("idx") int idx, @Param("boardIdx") int boardIdx) throws Exception;
    /* @Param 어노테이션을 이용하면 해당 파라미터들이 Map에 저장됨 */
}
