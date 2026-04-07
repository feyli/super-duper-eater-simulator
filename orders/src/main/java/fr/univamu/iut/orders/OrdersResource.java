package fr.univamu.iut.orders;

import fr.univamu.iut.orders.dao.OrderDAO;
import fr.univamu.iut.orders.model.Order;
import fr.univamu.iut.orders.model.OrderInput;
import fr.univamu.iut.orders.model.OrderItemInput;
import fr.univamu.iut.orders.model.OrderUpdateInput;
import fr.univamu.iut.orders.service.MenusAPIClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;

/**
 * REST resource for managing orders.
 * Implements all CRUD operations defined in the OpenAPI specification.
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrdersResource {
    
    private final OrderDAO orderDAO;
    
    public OrdersResource() {
        this.orderDAO = new OrderDAO();
    }
    
    /**
     * GET /orders
     * List all orders, optionally filtered by subscriberId.
     * 
     * @param subscriberId optional query parameter to filter by subscriber
     * @return 200 with list of orders
     */
    @GET
    public Response getAllOrders(@QueryParam("subscriberId") Integer subscriberId) {
        try {
            List<Order> orders = orderDAO.getAllOrders(subscriberId);
            return Response.ok(orders).build();
        } catch (SQLException e) {
            System.out.println("Error fetching orders: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Database error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * GET /orders/{id}
     * Get a single order by its ID.
     * 
     * @param id the order ID
     * @return 200 with order, or 404 if not found
     */
    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") int id) {
        try {
            Order order = orderDAO.getOrderById(id);
            
            if (order == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Order not found\"}")
                        .build();
            }
            
            return Response.ok(order).build();
        } catch (SQLException e) {
            System.out.println("Error fetching order: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Database error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * POST /orders
     * Create a new order.
     * Validates menus exist via Menus API before creation.
     * 
     * @param input the order input data
     * @return 201 with created order and Location header, 400 for invalid data, 404 if menu not found
     */
    @POST
    public Response createOrder(OrderInput input) {
        // Validate input
        if (input == null || input.getItems() == null || input.getItems().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Order must contain at least one item\"}")
                    .build();
        }
        
        if (input.getDeliveryDate() == null || input.getDeliveryAddress() == null || 
                input.getDeliveryAddress().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Delivery address and date are required\"}")
                    .build();
        }
        
        // Validate quantities
        for (OrderItemInput item : input.getItems()) {
            if (item.getQuantity() < 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Item quantity must be at least 1\"}")
                        .build();
            }
        }
        
        try {
            Order createdOrder = orderDAO.createOrder(input);
            
            URI location = URI.create("/orders/" + createdOrder.getId());
            
            return Response.created(location)
                    .entity(createdOrder)
                    .build();
                    
        } catch (MenusAPIClient.MenuNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        } catch (IOException e) {
            System.out.println("Error connecting to Menus API: " + e.getMessage());
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("{\"error\": \"Menus API unavailable: " + e.getMessage() + "\"}")
                    .build();
        } catch (SQLException e) {
            System.out.println("Error creating order: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Database error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * PUT /orders/{id}
     * Update an order's delivery address and date.
     * Order items cannot be modified.
     * 
     * @param id the order ID
     * @param input the update data
     * @return 200 with updated order, or 404 if not found
     */
    @PUT
    @Path("/{id}")
    public Response updateOrder(@PathParam("id") int id, OrderUpdateInput input) {
        // Validate input
        if (input == null || input.getDeliveryAddress() == null || 
                input.getDeliveryAddress().trim().isEmpty() || input.getDeliveryDate() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Delivery address and date are required\"}")
                    .build();
        }
        
        try {
            Order updatedOrder = orderDAO.updateOrder(id, input);
            
            if (updatedOrder == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Order not found\"}")
                        .build();
            }
            
            return Response.ok(updatedOrder).build();
        } catch (SQLException e) {
            System.out.println("Error updating order: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Database error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
    
    /**
     * DELETE /orders/{id}
     * Cancel/delete an order.
     * 
     * @param id the order ID
     * @return 204 if deleted, or 404 if not found
     */
    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") int id) {
        try {
            boolean deleted = orderDAO.deleteOrder(id);
            
            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Order not found\"}")
                        .build();
            }
            
            return Response.noContent().build();
        } catch (SQLException e) {
            System.out.println("Error deleting order: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Database error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}
