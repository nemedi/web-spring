package demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

public class TalkServlet extends HttpServlet {
	
	private static final List<String> messages = Collections.synchronizedList(new ArrayList<String>());

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int index = request.getParameterMap().containsKey("index")
				&& !request.getParameter("index").isEmpty()
				? Integer.parseInt(request.getParameter("index")) : 0;
		StringBuilder builder = new StringBuilder();
		builder.append(messages.size());
		if (index < messages.size()) {
			for (int i = index; i < messages.size(); i++) {
				builder.append("|").append(messages.get(i));
			}
		}
		response.setStatus(HttpStatus.OK_200);
		response.getWriter().write(builder.toString());
	}
	
	public static List<String> getMessages() {
		return messages;
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		messages.add(request.getReader()
				.lines()
				.reduce("",
						(accumulator, actual) -> accumulator + actual));
	}

}
