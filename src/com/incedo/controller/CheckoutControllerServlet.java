/**
 * 
 */
package com.incedo.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.incedo.service.EventService;
import com.incedo.vos.EventSubmitRequestVO;
import com.incedo.vos.ExperimentVariantVo;


/**
 * @author Deb
 *
 */
@WebServlet(urlPatterns = { "/checkout" })
public class CheckoutControllerServlet extends HttpServlet {

	public CheckoutControllerServlet() {
		super();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String userId = request.getParameter("userId");
    	System.out.println("userId--->"+userId);
    	EventService eventService = new EventService();
    	ExperimentVariantVo experimentVariantVo = eventService.getEventJsonFromServiceAPI(userId);
    	if(eventService.incedoGetVariantToken(experimentVariantVo).equalsIgnoreCase("life_style_model1")) {
			showPromoHeader(request, response, experimentVariantVo);
		} else {
			showNormalHeader(request, response, experimentVariantVo);
		}
    	
		EventSubmitRequestVO eventSubmit = eventService.incedoEvent(experimentVariantVo, eventService.getStage("checkout"));
		System.out.println("eventSubmit::::Gridwal::::"+eventSubmit.toString());
		eventService.pushNewEvent(eventSubmit);
    }
    
    public void showPromoHeader(HttpServletRequest request, HttpServletResponse response, ExperimentVariantVo experimentVariantVo) throws ServletException, IOException {
    	String userId = request.getParameter("userId");
    	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/gridwall.jsp");
    	request.setAttribute("eventSubmit", experimentVariantVo);
        request.setAttribute("nextPage", null);
        request.setAttribute("image", "Display Promo Image");
        request.setAttribute("userId", userId);
        request.setAttribute("imageName", request.getContextPath()+"/images/checkout_blue.png");
        dispatcher.forward(request, response);
    }
    
    public void showNormalHeader(HttpServletRequest request, HttpServletResponse response, ExperimentVariantVo experimentVariantVo) throws ServletException, IOException {
    	String userId = request.getParameter("userId");
    	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/gridwall.jsp");
    	request.setAttribute("eventSubmit", experimentVariantVo);
        request.setAttribute("nextPage", null);
        request.setAttribute("image", "Display Promo Image");
        request.setAttribute("userId", userId);
        request.setAttribute("imageName", request.getContextPath()+"/images/checkout_red.png");
        dispatcher.forward(request, response);
    }
	
}
