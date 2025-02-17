package com.grim.paint.model.service;


import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.grim.auth.model.vo.CustomUserDetails;
import com.grim.auth.service.AuthentlcationService;
import com.grim.paint.model.dto.PaintDTO;
import com.grim.paint.model.dto.PaintPicDTO;
import com.grim.paint.model.dto.SearchPaintDTO;
import com.grim.paint.model.mapper.PaintMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaintServiceImpl implements PaintService {

    private final PaintMapper mapper;
    private final FileService fileService;
    private final AuthentlcationService authService;

    @Override
    @Transactional
    public void save(PaintDTO board, MultipartFile file) {
        CustomUserDetails user = authService.getAuthenticatedUser();
        board.setUserNo(user.getUserNo());
        mapper.savePaintBoard(board);
        
        if (file != null && !file.isEmpty()) {
            String fileName = fileService.store(file);  
            String fileDownloadUri = "http://localhost/upfiles/" + fileName;
            // log.info("filename = {}", fileName);
            PaintPicDTO paintPicDto = new PaintPicDTO();
            paintPicDto.setPicName(fileDownloadUri);  
            paintPicDto.setPicBoardNo(board.getPicBoardNo());
            mapper.savePaint(paintPicDto);
        }
    }

    @Override
    public List<SearchPaintDTO> findAll(int page) {
        int size = 15;
        RowBounds rowBounds = new RowBounds(page * size, size);
        List<SearchPaintDTO> list = mapper.findAll(rowBounds);
        log.info("list = {}", list);
        return list;
    }

    @Override
    @Transactional
    public void update(PaintDTO board) {
    	log.info("엄준식{}:", board);
        CustomUserDetails user = authService.getAuthenticatedUser();
        long authenticatedUserNo = user.getUserNo();
        log.info("코레와 제로다 : {}", board.getPicBoardNo());
        PaintDTO existingBoard = mapper.findById(board.getPicBoardNo());
        log.info("유자남바데스{}", existingBoard.getUserNo());
        log.info("어센티케이션남바데스{}",authenticatedUserNo);
        if (existingBoard != null && existingBoard.getUserNo() == authenticatedUserNo) {
            board.setUserNo(authenticatedUserNo);
            mapper.updatePaintBoard(board);
        } else {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
    } 
}


