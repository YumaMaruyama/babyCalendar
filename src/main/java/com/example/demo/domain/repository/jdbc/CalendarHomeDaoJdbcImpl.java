package com.example.demo.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.model.CalendarDetailDTO;
import com.example.demo.domain.repository.CalendarHomeDao;

@Repository
public class CalendarHomeDaoJdbcImpl implements CalendarHomeDao {

	@Autowired
	JdbcTemplate jdbc;

	public int insertOne(CalendarDetailDTO calendarDetailDTO) {
		int rowNumber = jdbc.update("insert into holiday(holiday)"
				+ " value(?)", calendarDetailDTO.getHoliday());

		return rowNumber;
	}

	public int deleteOne(String holidayAdd) {
		int rowNumber = jdbc.update("delete from holiday where holiday = ?",holidayAdd);

		return rowNumber;

	}

	public List<CalendarDetailDTO> selectMany(int getYear, int getMonth) {

		//月が一桁の場合は0始まりの2桁に置換
		String getFormatMonth = String.format("%02d", getMonth);
		String stringYear = String.valueOf(getYear);
		String stringMonth = String.valueOf(getFormatMonth);
		String getYearMonth = stringYear.concat(stringMonth);
		//年と月合わせた六桁をDATE_FORMATで使用するパラメーターに付ける
		List<Map<String, Object>> calendarDetailList = jdbc
				.queryForList("select * from holiday where DATE_FORMAT(holiday, '%Y%m') = ?", getYearMonth);

		List<CalendarDetailDTO> getList = new ArrayList<>();
		for (Map<String, Object> map : calendarDetailList) {
			CalendarDetailDTO calendarDetaildto = new CalendarDetailDTO();
			calendarDetaildto.setHoliday((Date) map.get("holiday"));
			getList.add(calendarDetaildto);

		}

		return getList;

	}

}
