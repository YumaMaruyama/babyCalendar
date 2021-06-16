package com.example.demo.domain.repository;

import java.util.List;

import com.example.demo.domain.model.CalendarDetailDTO;

public interface CalendarHomeDao {

	public int insertOne(CalendarDetailDTO calendarDeteilDTO);

	public int deleteOne(String holidayAdd);

	public List<CalendarDetailDTO> selectMany(int getYear,int getMonth);
}
