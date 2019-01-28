package org.landy.javai18ndemo.javaee;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * TODO...
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/28
 */
@WebServlet(urlPatterns = "/locale")
public class LocaleServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {

        Locale locale = request.getLocale();
        long number = Long.parseLong(request.getParameter("num"));
        Format format = NumberFormat.getNumberInstance(locale);
        response.getWriter().println(format.format(number));
    }
}
