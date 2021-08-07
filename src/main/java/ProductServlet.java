import models.Product;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(urlPatterns = {""})
public class ProductServlet extends HttpServlet {
    ArrayList<Product> list = new ArrayList<>();
    ArrayList<Product> cart = new ArrayList<>();

    @Override
    public void init() throws ServletException {
        list.add(new Product(1, "Xe 1", "img/anh1.jpeg", 50000));
        list.add(new Product(2, "Xe 2", "img/anh2.jpeg", 50000));
        list.add(new Product(3, "Xe 3", "img/anh3.jpeg", 50000));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        RequestDispatcher requestDispatcher;

        HttpSession session = req.getSession();
        String userName = session.getAttribute("userName").toString();

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "create":
                resp.sendRedirect("CreateProduct.jsp");
                break;
            case "delete":
                int indexDelete = Integer.parseInt(req.getParameter("index"));
                list.remove(indexDelete);
                req.setAttribute("listProduct", list);
                requestDispatcher = req.getRequestDispatcher("home.jsp");
                requestDispatcher.forward(req, resp);
                break;
            case "edit":
                int indexEdit = Integer.parseInt(req.getParameter("index"));
                Product product = list.get(indexEdit);
                req.setAttribute("product", product);
                requestDispatcher = req.getRequestDispatcher("editProduct.jsp");
                requestDispatcher.forward(req, resp);
                break;
            case "addCart":
                int id = Integer.parseInt(req.getParameter("id"));
                Product product1 = findById(id);
                if (product1 != null) {
                    cart.add(product1);
                }

                session.setAttribute("cart",cart);

                req.setAttribute("listProduct", list);
                requestDispatcher = req.getRequestDispatcher("home.jsp");
                requestDispatcher.forward(req, resp);

                break;
            case "showCart":
                req.setAttribute("listCart", cart);
                requestDispatcher = req.getRequestDispatcher("cartProduct.jsp");
                requestDispatcher.forward(req, resp);
                break;
            default:
                req.setAttribute("listProduct", list);
                req.setAttribute("userName", userName);
                requestDispatcher = req.getRequestDispatcher("home.jsp");
                requestDispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        String img = req.getParameter("img");
        int price = Integer.parseInt(req.getParameter("price"));

        list.add(new Product(id, name, img, price));
        // là 1 cách để chuển sang servlet khác.
        resp.sendRedirect("/");

    }

    public Product findById(int id) {
        for (Product p : list) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

}
