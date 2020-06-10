package com.example.springboot.common;

import com.example.springboot.board.dto.BoardFileDto;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component  // 스프링의 빈으로 등록
public class FileUtils {

    public List<BoardFileDto> parseFileInfo(int boardIdx, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        if(ObjectUtils.isEmpty(multipartHttpServletRequest)) {
            return null;
        }

        // 파일이 업로드될 폴더 생성
        // yyyyMMdd 형식으로 폴더 생성 (해당 폴더가 없을 경우에만)
        List<BoardFileDto> fileList = new ArrayList<>();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        ZonedDateTime current = ZonedDateTime.now();    //오늘의 날짜 확인용
        String path = "images/" + current.format(format);
        File file = new File(path);
        if(file.exists() == false) {
            file.mkdirs();
        }

        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

        String newFileName, originalFileExtension, contentType;

        while(iterator.hasNext()) {
            List<MultipartFile> list = multipartHttpServletRequest.getFiles(iterator.next());
            for(MultipartFile multipartFile : list) {
                if(multipartFile.isEmpty() == false) {
                    contentType = multipartFile.getContentType();   // 파일 형식 확인
                    if(ObjectUtils.isEmpty(contentType)) {
                        break;
                    }
                    else {
                        // 파일 형식에 따른 확장자 지정
                        // 확장자는 쉽게 바꿀 수 있기 때문에 실제 파일의 형식과 확장자가 다를 수 있고, 파일의 위변조 확인 불가능
                        // -> nio 패키지를 이용하거나 아파치 티카와 같은 라이브러리 이용
                        if(contentType.contains("image/jpeg")) {
                            originalFileExtension = ".jpg";
                        } else if(contentType.contains("image/png")) {
                            originalFileExtension = ".png";
                        } else if(contentType.contains("image/gif")) {
                            originalFileExtension = ".gif";
                        } else {
                            break;
                        }
                    }

                    // 서버에 저장될 파일 이름 생성
                    // 중복되지 않는 값(nano time)으로 지정 (millisecond는 중복될 가능성이 있음)
                    newFileName = Long.toString(System.nanoTime()) + originalFileExtension;

                    // DB에 저장할 파일정보 세팅
                    BoardFileDto boardFile = new BoardFileDto();
                    boardFile.setBoardIdx(boardIdx);
                    boardFile.setFileSize(multipartFile.getSize()); // 파일 크기
                    boardFile.setOriginalFileName(multipartFile.getOriginalFilename()); // 파일의 원래 이름
                    boardFile.setStoredFilePath(path + "/" + newFileName); // 파일이 저장된 경로
                    fileList.add(boardFile);

                    // 업로드된 파일을 새로운 이름으로 변경 후 지정된 경로에 저장
                    file = new File(path + "/" + newFileName);
                    multipartFile.transferTo(file);
                }
            }
        }
        return fileList;
    }
}
