# SafeEat

## collections

- [advertisements](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/advertisements)
- [addresses](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/addresses)
- [allergies](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/allergies)
- [authentication](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/authentication)
- [carts](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/carts)
- [categories](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/categories)
- [deliveries](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/deliveries)
- [feedbacks](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/feedbacks)
- [home](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/home)
- [ingredients](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/ingredients)
- [items](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/items)
- [notifications](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/notifications)
- [orders](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/orders)
- [payments](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/payments)
- [product-sections](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/product-sections)
- [products](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/products)
- [restaurant-sections](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/restaurant-sections)
- [restaurants](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/restaurants)
- [users](https://a6aa0d3f-61e2-4430-accb-0eff3e181a75.mock.pstmn.io/users)

## endpoints

- advertisements
    - getAll -> GET: /advertisements/
    - getById -> GET: /advertisements/{id}
    - getByRestaurant -> GET: /advertisements/restaurant/{id}
    - create -> POST: /advertisements/
    - update -> PUT: /advertisements/{id}
    - delete -> DELETE: /advertisements/{id}

- addresses
    - getAll -> GET: /addresses/
    - getById -> GET: /addresses/{id}
    - getByUser -> GET: /addresses/user/{id}
    - create -> POST: /addresses/
    - update -> PUT: /addresses/{id}
    - delete -> DELETE: /addresses/{id}

- allergies
    - getAll -> GET: /allergies/
    - getById -> GET: /allergies/{id}
    - getByUser -> GET: /allergies/user/{id}
    - create -> POST: /allergies/
    - update -> PUT: /allergies/{id}
    - delete -> DELETE: /allergies/{id}
    
- authentication
    - login -> POST: /login
    - login -> POST: /signup

- carts
    - getAll -> GET: /carts/
    - getById -> GET: /carts/{id}
    - getByUser -> GET: /carts/user/{id}
    - create -> POST: /carts/
    - update -> PUT: /carts/{id}
    - delete -> DELETE: /carts/{id}

- categories
    - getAll -> GET: /categories/
    - getById -> GET: /categories/{id}
    - create -> POST: /categories/
    - update -> PUT: /categories/{id}
    - delete -> DELETE: /categories/{id}

- deliveries
    - getAll -> GET: /carts/
    - getById -> GET: /carts/{id}
    - getByRestaurant -> GET: /carts/restaurant/{id}
    - create -> POST: /carts/
    - update -> PUT: /carts/{id}
    - delete -> DELETE: /carts/{id}

- feed-backs
    - getAll -> GET: /feed-backs/
    - getById -> GET: /feed-backs/{id}
    - getByRestaurant -> GET: /feed-backs/restaurant/{id}
    - getByOrder -> GET: /feed-backs/order/{id}
    - create -> POST: /feed-backs/
    - update -> PUT: /feed-backs/{id}
    - delete -> DELETE: /feed-backs/{id}

- home
    - getAll -> GET: /home/
    - getById -> GET: /home/{id}
    - getByUser -> GET: /home/user/{id}
    - create -> POST: /home/
    - update -> PUT: /home/{id}
    - delete -> DELETE: /home/{id}

- ingredients
    - getAll -> GET: /ingredients/
    - getById -> GET: /ingredients/{id}
    - getByName -> GET: /ingredients/name/{id}
    - getByProduct -> GET: /ingredients/product/{id}
    - create -> POST: /ingredients/
    - update -> PUT: /ingredients/{id}
    - delete -> DELETE: /ingredients/{id}
    
- items
    - getAll -> GET: /items/
    - getById -> GET: /items/{id}
    - getByCart -> GET: /items/cart/{id}
    - getByOrder -> GET: /items/order/{id}
    - create -> POST: /items/
    - update -> PUT: /items/{id}
    - delete -> DELETE: /items/{id}

- notifications
    - getAll -> GET: /notifications/
    - getById -> GET: /notifications/{id}
    - getByUser -> GET: /notifications/user/{id}
    - create -> POST: /notifications/
    - update -> PUT: /notifications/{id}
    - delete -> DELETE: /notifications/{id}

- orders
    - getAll -> GET: /orders/
    - getById -> GET: /orders/{id}
    - getByUser -> GET: /orders/user/{id}
    - getByRestaurant -> GET: /orders/restaurant/{id}
    - create -> POST: /orders/
    - update -> PUT: /orders/{id}
    - delete -> DELETE: /orders/{id}

- payments
    - getAll -> GET: /payments/
    - getById -> GET: /payments/{id}
    - getByUser -> GET: /payments/user/{id}
    - create -> POST: /payments/
    - update -> PUT: /payments/{id}
    - delete -> DELETE: /payments/{id}

- product-sections
    - getAll -> GET: /product-section/
    - getById -> GET: /product-section/{id}
    - getByRestaurant -> GET: /product-section/restaurant/{id}
    - create -> POST: /product-section/
    - update -> PUT: /product-section/{id}
    - delete -> DELETE: /product-section/{id}

- products
    - getAll -> GET: /products/
    - getById -> GET: /products/{id}
    - getByRestaurant -> GET: /products/restaurant/{id}
    - create -> POST: /products/
    - update -> PUT: /products/{id}
    - delete -> DELETE: /products/{id}

- restaurants-sections
    - getAll -> GET: /restaurant-sections/
    - getById -> GET: /restaurant-sections/{id}
    - create -> POST: /restaurant-sections/
    - update -> PUT: /restaurant-sections/{id}
    - delete -> DELETE: /restaurant-sections/{id}

- restaurants
    - getAll -> GET: /restaurants/
    - getById -> GET: /restaurants/{id}
    - create -> POST: /restaurants/
    - update -> PUT: /restaurants/{id}
    - delete -> DELETE: /restaurants/{id}

- users
    - getAll -> GET: /users/
    - getById -> GET: /users/{id}
    - create -> POST: /users/
    - update -> PUT: /users/{id}
    - delete -> DELETE: /users/{id}
