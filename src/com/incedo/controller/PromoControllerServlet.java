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
@WebServlet(urlPatterns = { "/promoPage" })
public class PromoControllerServlet extends HttpServlet {

	public PromoControllerServlet() {
		super();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String userId = request.getParameter("userId");
    	EventService eventService = new EventService();
    	// Get bucket details
    	ExperimentVariantVo experimentVariantVo = eventService.getEventJsonFromServiceAPI(userId);
    	if(eventService.incedoGetVariantToken(experimentVariantVo).equalsIgnoreCase("life_style_model1")) {
			showPromoHeader(request, response, experimentVariantVo);
		} else {
			showNormalHeader(request, response, experimentVariantVo);
		}
    	
    	// Create Event VO
		EventSubmitRequestVO eventSubmit = eventService.incedoEvent(experimentVariantVo, eventService.getStage("promo"));
		System.out.println("eventSubmit::::Gridwal::::"+eventSubmit.toString());
		
		// Post event to Kinesis
		eventService.pushNewEvent(eventSubmit);
    }
    
    public void showPromoHeader(HttpServletRequest request, HttpServletResponse response, ExperimentVariantVo experimentVariantVo) throws ServletException, IOException {
    	String userId = request.getParameter("userId");
    	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/gridwall.jsp");
    	request.setAttribute("eventSubmit", experimentVariantVo);
        request.setAttribute("nextPage", request.getContextPath()+"/checkout");
        request.setAttribute("image", "Display Promo Image");
        request.setAttribute("userId", userId);
        request.setAttribute("imageName", request.getContextPath()+"/images/cart_blue.png");
        dispatcher.forward(request, response);
    }
    
    public void showNormalHeader(HttpServletRequest request, HttpServletResponse response, ExperimentVariantVo experimentVariantVo) throws ServletException, IOException {
    	String userId = request.getParameter("userId");
    	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/gridwall.jsp");
    	request.setAttribute("eventSubmit", experimentVariantVo);
        request.setAttribute("nextPage", request.getContextPath()+"/checkout");
        request.setAttribute("image", "Display Promo Image");
        request.setAttribute("userId", userId);
        request.setAttribute("imageName", request.getContextPath()+"/images/cart_red.png");
        dispatcher.forward(request, response);
    }
	
}
