import { Navigate, Route, Routes } from 'react-router-dom'
import { MainLayout } from '@/layouts/MainLayout'
import { LoginPage } from '@/pages/auth/LoginPage'
import { RegisterPage } from '@/pages/auth/RegisterPage'
import { CartPage } from '@/pages/client/CartPage'
import { CheckoutPage } from '@/pages/client/CheckoutPage'
import { HomePage } from '@/pages/client/HomePage'
import { OrderDetailPage } from '@/pages/client/OrderDetailPage'
import { OrdersPage } from '@/pages/client/OrdersPage'
import { ProductDetailPage } from '@/pages/client/ProductDetailPage'
import { ProfilePage } from '@/pages/client/ProfilePage'
import { RestaurantDetailPage } from '@/pages/client/RestaurantDetailPage'
import { CreateRestaurantPage } from '@/pages/owner/CreateRestaurantPage'
import { EditProductPage } from '@/pages/owner/EditProductPage'
import { EditRestaurantPage } from '@/pages/owner/EditRestaurantPage'
import { OwnerDashboardPage } from '@/pages/owner/OwnerDashboardPage'
import { OwnerOrderDetailPage } from '@/pages/owner/OwnerOrderDetailPage'
import { OwnerOrdersPage } from '@/pages/owner/OwnerOrdersPage'
import { OwnerProductsPage } from '@/pages/owner/OwnerProductsPage'
import { OwnerRoute } from '@/routes/OwnerRoute'
import { ProtectedRoute } from '@/routes/ProtectedRoute'

export function App() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/restaurants/:id" element={<RestaurantDetailPage />} />
          <Route path="/products/:id" element={<ProductDetailPage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/checkout" element={<CheckoutPage />} />
          <Route path="/orders" element={<OrdersPage />} />
          <Route path="/orders/:id" element={<OrderDetailPage />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/restaurant/new" element={<CreateRestaurantPage />} />

          <Route element={<OwnerRoute />}>
            <Route path="/restaurant" element={<OwnerDashboardPage />} />
            <Route path="/restaurant/edit" element={<EditRestaurantPage />} />
            <Route path="/restaurant/products" element={<OwnerProductsPage />} />
            <Route path="/restaurant/products/:id/edit" element={<EditProductPage />} />
            <Route path="/restaurant/orders" element={<OwnerOrdersPage />} />
            <Route path="/restaurant/orders/:id" element={<OwnerOrderDetailPage />} />
          </Route>
        </Route>

        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  )
}
