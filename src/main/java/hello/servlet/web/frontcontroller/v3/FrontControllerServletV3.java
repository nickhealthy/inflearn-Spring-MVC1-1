package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

  private Map<String, ControllerV3> controllerMap = new HashMap<>();

  public FrontControllerServletV3() {
    controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
    controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
    controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String requestURI = request.getRequestURI();
    ControllerV3 controller = controllerMap.get(requestURI);
    if (controller == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    // request로 넘어온 파라미터를 Map에 넣기
    Map<String, String> paramMap = createParamMap(request);
    ModelView modelView = controller.process(paramMap);

    String viewName = modelView.getViewName();
    MyView view = viewReslover(viewName);
    view.render(modelView.getModel(), request, response);
  }

  private MyView viewReslover(String viewName) {
    return new MyView("/WEB-INF/views/" + viewName + ".jsp");
  }

  private Map<String, String> createParamMap(HttpServletRequest request) {
    Map<String, String> paramMap = new HashMap<>();
    request.getParameterNames().asIterator()
        .forEachRemaining(param -> paramMap.put(param, request.getParameter(param)));

    // 결과 찍어보기
    request.getParameterNames().asIterator()
        .forEachRemaining(param -> System.out.println(Arrays.toString(new String[]{param})));
    System.out.println("paramMap = " + paramMap);

    return paramMap;
  }
}
