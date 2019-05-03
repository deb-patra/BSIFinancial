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

import com.incedo.constants.EventConstants;
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
    	String userId = request.getParameter(EventConstants.USERID);
    	EventService eventService = new EventService();
    	// Get bucket details
    	ExperimentVariantVo experimentVariantVo = eventService.getEventJsonFromServiceAPI(userId);
		
    	if(eventService.incedoGetVariantToken(experimentVariantVo).contains(eventService.getConfiguredProperty(EventConstants.PROMOVARIANT1))) {
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
    	EventService eventService = new EventService();
    	String userId = request.getParameter(EventConstants.USERID);
    	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(eventService.getConfiguredProperty(EventConstants.JSPPAGENAME));
    	request.setAttribute(EventConstants.EVENT_SUBMIT, experimentVariantVo);
        request.setAttribute(EventConstants.NEXTPAGE, request.getContextPath()+EventConstants.CHECKOUTPAGE);
        request.setAttribute(EventConstants.USERID, userId);
        request.setAttribute(EventConstants.IMAGENAME, request.getContextPath()+eventService.getConfiguredProperty(EventConstants.PROMOVARIANT1IMAGE));
        dispatcher.forward(request, response);
    }
    
    public void showNormalHeader(HttpServletRequest request, HttpServletResponse response, ExperimentVariantVo experimentVariantVo) throws ServletException, IOException {
    	EventService eventService = new EventService();
    	String userId = request.getParameter(EventConstants.USERID);
    	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(eventService.getConfiguredProperty(EventConstants.JSPPAGENAME));
    	request.setAttribute(EventConstants.EVENT_SUBMIT, experimentVariantVo);
        request.setAttribute(EventConstants.NEXTPAGE, request.getContextPath()+EventConstants.CHECKOUTPAGE);
        request.setAttribute(EventConstants.USERID, userId);
        request.setAttribute(EventConstants.IMAGENAME, request.getContextPath()+eventService.getConfiguredProperty(EventConstants.PROMOVARIANT2IMAGE));
        dispatcher.forward(request, response);
    }
}
