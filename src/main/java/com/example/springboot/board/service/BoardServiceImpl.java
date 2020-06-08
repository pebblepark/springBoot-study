package com.example.springboot.board.service;

import com.example.springboot.board.dto.BoardDto;
import com.example.springboot.board.mapper.BoardMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardMapper boardMapper;

    @Override
    public List<BoardDto> selectBoardList() throws Exception {
        List<BoardDto> list = boardMapper.selectBoardList();
        list.forEach(board -> {
            log.info(board.toString());
        });
        return list;
    }

    @Override
    public void insertBoard(BoardDto board) throws Exception {
        log.info(board.toString() + "----------------------ServiceImpl-------------------------");
        boardMapper.insertBoard(board);
        log.info(board.toString() + "----------------------After Insert------------------------");
    }

    @Override
    public BoardDto selectBoardDetail(int boardIdx) throws Exception {
        BoardDto board = boardMapper.selectBoardDetail(boardIdx);
        boardMapper.updateHitCount(boardIdx);

        return board;
    }

    @Override
    public void updateBoard(BoardDto board) throws Exception {
        boardMapper.updateBoard(board);
    }

    @Override
    public void deleteBoard(int boardIdx) throws Exception {
        boardMapper.deleteBoard(boardIdx);
    }
}	
