package com.grim.museum.model.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.grim.museum.model.dto.MuseumDTO;
import com.grim.museum.model.mapper.MuseumMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MuseumServiceImpl implements MuseumService {
	
	private final MuseumMapper mapper;
	
	// 미술관 세울 매물 API
	public String getApiMuseum(int page) {
		
		String requestUrl = "http://apis.data.go.kr/B190017/service/GetBankruptcyEstatesGoodsService2020081/getRealEstateList2020081";
			   requestUrl += "?serviceKey=EhfazFkGEi%2B9n0cRDjEs%2FHz7D8Bvej8fga6MidRbq5kjfXEeVcwgl8HTqJNwCYjbz6KmcaFNS9rMFf2ADRJw5g%3D%3D";
			   requestUrl += "&pageNo=" + page;
			   requestUrl += "&numOfRows=144";
			   requestUrl += "&resultType=json";
			   try {
				requestUrl += "&progStutNm=" + URLEncoder.encode("매각검토", "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		URI uri = null;
		try {
			uri = new URI(requestUrl);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(uri, String.class);
		
		return response;
	}

	// 실제 미술관 API
	@Override
	public String getRealMuseum(int page) {
		String requestUrl = "https://api.vworld.kr/req/data";
			   requestUrl += "?service=data";
			   requestUrl += "&request=GetFeature";
			   requestUrl += "&page=" + page;
			   requestUrl += "&data=LT_P_DGMUSEUMART";
			   requestUrl += "&key=0AE873A4-4246-3871-B49A-76BB7ADDEC56";
			   requestUrl += "&domain=localhost";
			   requestUrl += "&geomFilter=POINT(x y)";
			   
		URI uri = null;
		try {
			uri = new URI(requestUrl);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(uri, String.class);
		
		return response;
	}

	@Override
	public void saveMuseum(MuseumDTO museum) {
		if(museum.getMuseumName().equals("") || museum.getMuseumSidoname().equals("")) {
			System.out.println("예외발생");
		}
		mapper.saveMuseum(museum);
	
	}

}
