package com.example.demo.domain.model;

import lombok.Data;

@Data
public class CalendarHolidayAdditionForm {

	private int year;

	private int month;
	//ユーザーが入力した日付を複数格納する
	private int days;

	//日付をselectしない際はフラグを立てること
	private int is_deleted;
}
