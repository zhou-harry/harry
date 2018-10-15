package com.harry.fssc.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(schema = "DBUSER", name = "T_SIDE_CONTENT")
public class SideContent implements Serializable {

		private static final long serialVersionUID = 1L;
		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SIDE_CONTENT")
		@SequenceGenerator(sequenceName = "T_SIDE_CONTENT_SEQ", allocationSize = 1, name = "SEQ_SIDE_CONTENT")
		private Long id;
		@Column(name = "URL_KEY")
		private String key;
		private String title;
		private String icon;
		@Column(name = "PARENT_KEY")
		private String parentKey;
		private Boolean expanded;
		@Column(name = "SIDE_EXPANDED")
		private Boolean sideExpanded;
		private Boolean valid;
		@Column(name = "INDEX_")
		private Integer index;
		private Boolean fixed;
		@Column(name = "FORM_")
		private Boolean form;
		@Transient
		private List<SideContent> items; 
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getParentKey() {
			return parentKey;
		}
		public void setParentKey(String parentKey) {
			this.parentKey = parentKey;
		}
		public Boolean getExpanded() {
			return expanded;
		}
		public void setExpanded(Boolean expanded) {
			this.expanded = expanded;
		}
		public Boolean getSideExpanded() {
			return sideExpanded;
		}
		public void setSideExpanded(Boolean sideExpanded) {
			this.sideExpanded = sideExpanded;
		}
		public Boolean getValid() {
			return valid;
		}
		public void setValid(Boolean valid) {
			this.valid = valid;
		}
		public Integer getIndex() {
			return index;
		}
		public void setIndex(Integer index) {
			this.index = index;
		}
		public Boolean getFixed() {
			return fixed;
		}
		public void setFixed(Boolean fixed) {
			this.fixed = fixed;
		}
		public List<SideContent> getItems() {
			return items;
		}
		public void setItems(List<SideContent> items) {
			this.items = items;
		}
		public Boolean getForm() {
			return form;
		}
		public void setForm(Boolean form) {
			this.form = form;
		}
		public SideContent() {
			super();
		}
		public SideContent(String parentKey) {
			super();
			this.parentKey = parentKey;
		}
		
}
