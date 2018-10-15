package com.harry.bpm.jpa.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.harry.bpm.bean.CusTaskinst;

import java.util.List;

public interface CusTaskinstService {

	public void save(CusTaskinst bean);

}
