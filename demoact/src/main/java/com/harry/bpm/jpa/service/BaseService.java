package com.harry.bpm.jpa.service;

import java.util.List;

public interface BaseService<T> {

	public T save(T t);
	
	public void save(List<T> ts);
	
	public void delete(T t);

	public List<T> findAll(Class<T> c);
}
