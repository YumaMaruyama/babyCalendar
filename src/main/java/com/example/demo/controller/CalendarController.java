package com.example.demo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.domain.model.CalendarDetailDTO;
import com.example.demo.domain.model.CalendarHolidayAdditionForm;
import com.example.demo.domain.service.CalendarHomeService;

@Controller
public class CalendarController {

	@Autowired
	CalendarHomeService calendarHomeService;

	private String calendarProcess(Model model, int year, int month, int differentMonthParams) { //各ボタンを押したときの年と月の値を取得

		System.out.println(year + ":" + month + ":" + differentMonthParams);
		System.out.println("calendarProcess到達");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, (month + differentMonthParams));
		int getYear = calendar.get(Calendar.YEAR);
		int getMonth = calendar.get(Calendar.MONTH);

		System.out.println("年" + getYear);
		System.out.println("月" + (getMonth + 1));
		//htmlに0から11形式で渡す
		model.addAttribute("year", getYear);
		model.addAttribute("month", getMonth);

		//月始まりの曜日取得
		calendar.set(Calendar.DATE, 1);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK);
		System.out.println("weekday " + weekday);

		//月の最終日取得
		calendar.set(getYear, getMonth + 1, 1);
		calendar.add(Calendar.DATE, -1);
		int maxDay = calendar.get(Calendar.DATE);
		System.out.println("maxDay " + maxDay);

		//月の週の数取得
		int week = maxDay + (weekday - 1);
		int weekNumber = week / 7;
		if (week % 7 > 0) {
			weekNumber++;
		}
		System.out.println("週数  " + weekNumber);

		//日付を配列に挿入するための変数
		int dayInsert = 1;
		//配列の要素分繰り返すためのカウント変数
		int dayCount = 1;

		//二次元配列にカレンダーの日付を格納
		int[][] getDayList = new int[weekNumber][7];
		for (int forCount = 0; forCount < weekNumber * 7;) {
			if ((dayCount >= weekday) && (dayInsert <= maxDay)) {
				getDayList[forCount / 7][forCount % 7] = dayInsert;
				dayInsert++;
				forCount++;
				dayCount++;
			} else {
				getDayList[forCount / 7][forCount % 7] = 0;
				dayCount++;
				forCount++;
			}
		}
		System.out.println("二次元配列" + Arrays.deepToString(getDayList));
		model.addAttribute("getDayList", getDayList);

		//データベースからselectしてくるときにどの年の何月か指定して置いたら簡単になる
		int[] holidays = calendarHomeService.selectMany(getYear,getMonth + 1);
		System.out.println("データベースに格納されている日付" + Arrays.toString(holidays));

		int index = 0;
		List<Integer> holidaysList = new ArrayList<>();
		for (int i = 0; i < holidays.length; i++){
		    holidaysList.add(holidays[i]);
		}

		model.addAttribute("holidaysList",holidaysList);

		return "calendar/calendarHome";
	}

	//前の月ボタン用メソッド
	@PostMapping(value = "/calendarHome", params = "previous")
	public String getCalendarHomePrevious(Model model,
			@RequestParam("year") int year, @RequestParam("month") int month) {
		System.out.println("前の月メソッド");
		return calendarProcess(model, year, month, -1);
	}

	//今月ボタンとURLから検索用メソッド
	@GetMapping("/calendarHome")
	public String getCalendarHomeHome(Model model) {
		System.out.println("URL&今月用メソッド");
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		model.addAttribute("year", year);
		model.addAttribute("month", month);
		return calendarProcess(model, year, month, +0);
	}
	//次の月ボタン用メソッド
	@PostMapping(value = "/calendarHome", params = "tip")
	public String getCalendarHomeTip(Model model,
			@RequestParam("year") int year, @RequestParam("month") int month) {

		System.out.println("次の月メソッド");
		return calendarProcess(model, year, month, +1);
	}
	//休日追加ボタン用メソッド
	@GetMapping(value = "/calendarHoliday", params = "addition")
	public String getCalendarHolidayAddition(@ModelAttribute CalendarHolidayAdditionForm form, Model model,
			@RequestParam("month") int month,
			@RequestParam("year") int year) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		int getYear = calendar.get(Calendar.YEAR);
		int getMonth = calendar.get(Calendar.MONTH);

		calendar.set(getYear,getMonth + 1,1);
		calendar.add(Calendar.DATE, - 1);
		int maxDay = calendar.get(Calendar.DATE);
		System.out.println("今月の最大日" + maxDay);

		 String []maxDayList = new String[maxDay];

	        for(int i = 0; i < maxDayList.length; i++){
	            maxDayList[i] = String.valueOf(i + 1);
	        }
	        for(int i = 0; i < maxDayList.length; i++){
	            System.out.println(maxDayList[i]);
	        }

		model.addAttribute("maxDayList",maxDayList);

		model.addAttribute("year", getYear);
		model.addAttribute("month", +getMonth);
		return "calendar/calendarHolidayAddition";
	}

	@PostMapping(value = "/calendarHoliday", params = "addition")
	public String postCalendarHolidayAddition(@ModelAttribute CalendarHolidayAdditionForm form,@Validated BindingResult bindingResult, Model model,
			 @RequestParam("year") int year,@RequestParam("month") int month) throws ParseException {

		System.out.println("getYear " + form.getYear());
		System.out.println("getMonth " + (form.getMonth() + 1));
		System.out.println("getDay " + form.getDays());

		int getYear = form.getYear();
		int getMonth = form.getMonth() +1;
		int getDate = form.getDays();

		String holidayAdd = getYear + "/" + getMonth + "/" + getDate;

		SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd");
		try {
		Date date = sdf.parse(holidayAdd);
		System.out.println("date" + date);


		CalendarDetailDTO calendarDetaildto = new CalendarDetailDTO();

		calendarDetaildto.setHoliday(date);
		boolean result = calendarHomeService.insertOne(calendarDetaildto);

		if(result) {
			System.out.println("insert成功");
		}else {
			System.out.println("insert失敗");
		}

		}catch(ParseException e) {
			if (bindingResult.hasErrors()) {
				System.out.println("エラーチェック");
				model.addAttribute("result","正しい日付が入力されませんでした。");
				return getCalendarHolidayAddition(form,model,month,year);
			}
		}

			return calendarProcess(model,year,month,+0);
	}


	@GetMapping(value = "/calendarHoliday" ,params = "deletion")
	public String getCalendarHolidayDeletion(@ModelAttribute CalendarHolidayAdditionForm form,Model model,@RequestParam("year") int year,@RequestParam("month") int month) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, 1);
		int getYear = calendar.get(Calendar.YEAR);
		int getMonth = calendar.get(Calendar.MONTH);

		calendar.set(getYear, getMonth + 1,1);
		calendar.add(Calendar.DATE, - 1);
		int maxDay = calendar.get(Calendar.DATE);
		System.out.println("月の最大日 " + maxDay);

		String maxDayList[] = new String [maxDay];
		for(int i = 0; i < maxDayList.length; i++) {
			maxDayList[i] = String.valueOf(i + 1);
		}
		for(int i = 0; i < maxDayList.length; i++) {
			System.out.println("maxDayList " + maxDayList[i]);
		}

		model.addAttribute("maxDayList",maxDayList);

		model.addAttribute("year",getYear);
		model.addAttribute("month",getMonth);

		return "calendar/calendarHolidayDeletion";

	}
	@PostMapping(value = "/calendarHoliday" , params = "deletion")
	public String postCalendarHolidayDeletion(@ModelAttribute CalendarHolidayAdditionForm form,@Validated BindingResult bindingResult,Model model,@RequestParam("year") int year,@RequestParam("month") int month) {

		System.out.println("getYear " + form.getYear());
		System.out.println("getMonth " + (form.getMonth() + 1));
		System.out.println("getDay " + form.getDays());

		int getYear = form.getYear();
		int getMonth = form.getMonth() + 1;
		int getDays = form.getDays();

		String holidayAdd = getYear + "-" + getMonth + "-" + getDays;
		boolean holiday = calendarHomeService.deleteOne(holidayAdd);

		if(holiday) {
			System.out.println("削除成功");
		}else {
			System.out.println("削除失敗");
		}
			return calendarProcess(model,year,month,+0);
	}

	@GetMapping("/calendarHomeGuide")
	public String getCalendarHomeGuide() {
		return "calendar/calendarGuide";
	}


}
