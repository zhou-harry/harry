package com.harry.fssc.model.dto;

import java.io.Serializable;

public class Variables implements Serializable {

	private static final long serialVersionUID = 1L;

	private String _key;
	private Object _value;

	public String get_key() {
		return _key;
	}

	public void set_key(String _key) {
		this._key = _key;
	}

	public Object get_value() {
		return _value;
	}

	public void set_value(String _value) {
		this._value = _value;
	}

	/**
	 * @param _key
	 * @param _value
	 */
	public Variables(String _key, Object _value) {
		this._key = _key;
		this._value = _value;
	}

	/**
	 * 
	 */
	public Variables() {
	}

}
