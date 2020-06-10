package com.example.springboot.board.controller;

import com.example.springboot.board.dto.BoardDto;
import com.example.springboot.board.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class BoardController {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    // lombok 사용시 @Slf4j 어노테이션을 사용하면 따로 로거를 생성할 필요 x

    @Autowired
    private BoardService boardService;

    @RequestMapping("/logger/test")
    public void loggerTest() {
        log.trace("trace -- Hello world.");
        log.debug("debug -- Hello world.");
        log.info("info -- Hello world.");
        log.warn("warn -- Hello world.");
        log.error("error -- Hello world.");
    }

    @RequestMapping("/board/openBoardList.do")
    public ModelAndView openBoardList() throws Exception{
        log.debug("openBoardList");

        ModelAndView mv = new ModelAndView("board/boardList");

        List<BoardDto> list = boardService.selectBoardList();
        mv.addObject("list", list);

        return mv;
    }

    @RequestMapping("/board/openBoardWrite.do")
    public String openBoardWrite() throws Exception{
        return "board/boardWrite";
    }

    @RequestMapping("/board/insertBoard.do")
    public String insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception{
        log.info("insert ------------------ " + multipartHttpServletRequest.toString());
        boardService.insertBoard(board, multipartHttpServletRequest);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/openBoardDetail.do")
    public ModelAndView openBoardDetail(@RequestParam int boardIdx) throws Exception{
        ModelAndView mv = new ModelAndView("board/boardDetail");

        BoardDto board = boardService.selectBoardDetail(boardIdx);
        mv.addObject("board", board);

        return mv;
    }

    @RequestMapping("/board/updateBoard.do")
    public String updateBoard(BoardDto board) throws Exception{
        boardService.updateBoard(board);
        return "redirect:/board/openBoardList.do";
    }

    @RequestMapping("/board/deleteBoard.do")
    public String deleteBoard(int boardIdx) throws Exception{
        boardService.deleteBoard(boardIdx);
        return "redirect:/board/openBoardList.do";
    }
}
