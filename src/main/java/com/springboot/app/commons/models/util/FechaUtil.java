package com.springboot.app.commons.models.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class FechaUtil {

	public String convertirDateAString(Date date) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("es", "AR"));
			return formatter.format(date);
		} else {
			return null;
		}
	}

	public String convertirDateAStringWithoutTime(Date date) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", new Locale("es", "AR"));
			return formatter.format(date);
		} else {
			return null;
		}
	}

	public Date convertirLongToDate(Long date) {
		if (date != null) {
			Calendar fechaFuncionCalendar = Calendar.getInstance();
			fechaFuncionCalendar.setTimeInMillis(date);
			return fechaFuncionCalendar.getTime();
		} else {
			return null;
		}
	}
}
