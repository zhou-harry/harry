package com.harry.fssc.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.harry.fssc.aop.logger.BehaviorManage;
import com.harry.fssc.enums.AspectType;
import com.harry.fssc.model.Attachment;
import com.harry.fssc.result.ResponseData;
import com.harry.fssc.result.ResultCode;
import com.harry.fssc.service.AttachmentService;

@RestController
@RequestMapping("file")
public class AttachmentController {
	
	@Autowired
	private AttachmentService service;
	
	@RequestMapping(value = "preview", method = RequestMethod.GET)
	public void previewImg(HttpServletRequest request, HttpServletResponse response,String id)
			throws IOException {
		if (null==id) {
			return;
		}
		Optional<Attachment> optional = service.findById(id);
		try {
			Attachment attachment = optional.get();
			response.setHeader("content-disposition", "online;filename=" + attachment.getName());// 设置响应头 attachment
			response.setContentType(attachment.getType());
			OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
			toClient.write(attachment.getResource()); // 输出数据
			toClient.close();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@BehaviorManage(type=AspectType.FILE_UPLOAD)
	public ResponseData uploadAttachment(HttpServletRequest request, HttpServletResponse response) {
		StandardMultipartHttpServletRequest req=(StandardMultipartHttpServletRequest)request;
		Map<String, MultipartFile> fileMap = req.getFileMap();
		Set<String> ks = fileMap.keySet();
		MultipartFile file=null;
		for (String k : ks) {
			file = fileMap.get(k);
		}
		if (null==file) {
			return new ResponseData(ResultCode.FAILED, "附件为空.");
		}
		String fileid = UUID.randomUUID().toString();
//		String fileid = request.getParameter("fileid");
		try {
	        Attachment entity = new Attachment();
	        entity.setId(fileid);
	        entity.setName(file.getOriginalFilename());
	        entity.setType(file.getContentType());
	        entity.setResource(file.getBytes());
	        service.save(entity);
	        
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseData(ResultCode.FAILED, e.getMessage());
		}
		return new ResponseData(ResultCode.SUCCESS, fileid);
	}
}
