package com.example.demo.domain.model;

import java.util.Date;

import lombok.Data;

@Data
public class CalendarHolidayAdditionDTO {

	//追加した回数カウント
	private int id;
	//どの日を休日にするか
	private Date holidayAddition;
	//いつ休日を追加したか
	private Date registration_date;
}
