package com.example.demo.domain.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.CalendarDetailDTO;
import com.example.demo.domain.repository.CalendarHomeDao;

@Service
public class CalendarHomeService {

	@Autowired
	CalendarHomeDao dao;

	public boolean insertOne(CalendarDetailDTO calendarDetailDTO) {
		 int rowNumber = dao.insertOne(calendarDetailDTO);

		 boolean result = false;
		 if(rowNumber > 0) {
		 result = true;
		 }
		 return result;
	}

	public boolean deleteOne(String holidayAdd) {
		int rowNumber = dao.deleteOne(holidayAdd);

		boolean result = false;

		if(rowNumber > 0) {
			result = true;
		}

		return result;
	}

	public int[] selectMany(int getYear,int getMonth) {
		 List<CalendarDetailDTO> selectHoliday = dao.selectMany(getYear,getMonth);


		 //selectManyでdate型でとってきた値の日付のみ格納する
		 int[] holidays = new int[selectHoliday.size()];

		 Calendar calendar = Calendar.getInstance();

		 for(int i = 0; i < selectHoliday.size(); i++) {
			 //ListのCalendarDetailDTOから一つだけ取得する
			 CalendarDetailDTO calendarDetailDTO = selectHoliday.get(i);
			 //CalendarDetailDTOからHolidayを取得する
			 Date holiday = calendarDetailDTO.getHoliday();

			 calendar.setTime(holiday);
			 //Calendarクラスでholidayの日付のみ取得する
			 int day = calendar.get(Calendar.DATE);

			 holidays[i] = day;

		 }
		 //指定した年と月の日付が入った配列を返す
		 return holidays;
	}
}
