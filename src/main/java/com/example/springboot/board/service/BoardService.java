package com.example.springboot.board.service;

import com.example.springboot.board.dto.BoardDto;

import java.util.List;

public interface BoardService {
    List<BoardDto> selectBoardList() throws Exception;

    void insertBoard(BoardDto board) throws Exception;

    BoardDto selectBoardDetail(int boardIdx) throws Exception;

    void updateBoard(BoardDto board) throws Exception;

    void deleteBoard(int boardIdx) throws Exception;
}
