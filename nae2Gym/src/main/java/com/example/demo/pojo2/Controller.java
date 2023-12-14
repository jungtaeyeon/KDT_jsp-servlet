package com.example.demo.pojo2;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.pojo1.ActionForward;

public interface Controller {
	public String execute(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException;
}
