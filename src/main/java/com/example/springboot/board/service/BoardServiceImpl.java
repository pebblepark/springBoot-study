package com.example.springboot.board.service;

import com.example.springboot.board.dto.BoardDto;
import com.example.springboot.board.dto.BoardFileDto;
import com.example.springboot.board.mapper.BoardMapper;
import com.example.springboot.common.FileUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Log4j2
@Service
@Transactional  // 인터페이스나 클래스, 메서드에 사용 가능 / 어노테이션이 적용된 대상은 설정된 트랜잭션 빈에 의해서 트랜잭션이 처리됨
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private FileUtils fileUtils;

    @Override
    public List<BoardDto> selectBoardList() throws Exception {
        List<BoardDto> list = boardMapper.selectBoardList();
        list.forEach(board -> {
            log.info(board.toString());
        });
        return list;
    }

    @Override
    public void insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        /*if(ObjectUtils.isEmpty(multipartHttpServletRequest) == false) {
            Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
            String name;
            while(iterator.hasNext()) {
                name = iterator.next();
                log.debug("file tag name : " + name);
                List<MultipartFile> list = multipartHttpServletRequest.getFiles(name);
                for(MultipartFile multipartFile: list) {
                    log.debug("start file information ");
                    log.debug("file name : " + multipartFile.getOriginalFilename());
                    log.debug("file size : " + multipartFile.getSize());
                    log.debug("file content type : " + multipartFile.getContentType());
                    log.debug("end file information. \n");

                }
            }
        }*/
        boardMapper.insertBoard(board);
        List<BoardFileDto> list = fileUtils.parseFileInfo(board.getBoardIdx(), multipartHttpServletRequest);
        if(CollectionUtils.isEmpty(list) == false) {
            boardMapper.insertBoardFileList(list);
        }
    }

    @Override
    public BoardDto selectBoardDetail(int boardIdx) throws Exception {
        BoardDto board = boardMapper.selectBoardDetail(boardIdx);
        List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
        board.setFileList(fileList);

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
