package com.example.demo.domain.model;

import java.util.Date;

import lombok.Data;

@Data
public class CalendarDetailDTO {

	private Date holiday;

	private int is_deleted;
}
