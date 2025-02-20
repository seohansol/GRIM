package com.grim.museum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grim.museum.model.dto.MuseumDTO;
import com.grim.museum.model.service.MuseumService;
import com.grim.paint.model.dto.PaintDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
//@CrossOrigin("*")
@RequestMapping(value="museum", produces="application/json; charset=UTF-8")
public class MuseumController {
	
	private final MuseumService service;
	
	// 매물정보 가져오기
	@GetMapping("/apiMuseum")
	public ResponseEntity<?> getApiMuseum(@RequestParam(name="page") int page){
		
		String response = service.getApiMuseum(page);
		System.out.println(response);
		return ResponseEntity.ok(response);
	}
	
	// 실제 미술관 가져오기
	@GetMapping("/realMuseum")
	public ResponseEntity<?> getRealMuseum(@RequestParam(name="page") int page){
		
		String response = service.getRealMuseum(page);
		System.out.println(response);
		return ResponseEntity.ok().body(response);
	}
	
	// 미술관 창설 신청
	@PostMapping
	public ResponseEntity<String> saveMuseum(@Valid @RequestBody MuseumDTO museum){ 
		System.out.println(museum);
		service.saveMuseum(museum);	
		
		return ResponseEntity.status(HttpStatus.CREATED).body("미술관 창설 신청이 완료되었습니다.");
	}
	
	// 창설된 미술관 가져오기
	@GetMapping
	public ResponseEntity<List<MuseumDTO>> getSelectAllMuseum(){
		return ResponseEntity.ok(service.getSelectAllMuseum());
	}
	
	

}
