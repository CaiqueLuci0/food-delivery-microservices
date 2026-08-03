export type AddressRequest = {
  cep: string
  logradouro: string
  numero: string
  complemento?: string | null
  bairro: string
  cidade: string
  uf: string
  referencia?: string | null
}

export type AddressResponse = {
  id: string
  cep: string
  logradouro: string
  numero: string
  complemento?: string | null
  bairro: string
  cidade: string
  uf: string
  referencia?: string | null
  latitude?: number | null
  longitude?: number | null
}

export type User = {
  id: string
  name: string
  email: string
  address: AddressResponse
}

export type LoginRequest = {
  email: string
  password: string
}

export type LoginResponse = {
  token: string
  user: User
}

export type UserCreateRequest = {
  name: string
  email: string
  password: string
  address: AddressRequest
}

export type UserUpdateRequest = {
  name: string
  address: AddressRequest
}

export type Restaurant = {
  id: string
  name: string
  description: string
  ownerId: string
  imageKey?: string | null
  imageUrl?: string | null
  address: AddressResponse
}

export type RestaurantWriteRequest = {
  name: string
  description: string
  address: AddressRequest
}

export type SpecOption = {
  id: string
  name: string
  description?: string | null
  extraPrice: number
}

export type Specification = {
  id: string
  name: string
  description?: string | null
  specOptions: SpecOption[]
}

export type SpecOptionRequest = {
  name: string
  description?: string | null
  extraPrice: number
}

export type SpecificationRequest = {
  name: string
  description?: string | null
  specOptions?: SpecOptionRequest[]
}

export type Product = {
  id: string
  name: string
  price: number
  description?: string | null
  ownerId: string
  restaurantId: string
  imageKey?: string | null
  imageUrl?: string | null
  specifications: Specification[]
}

export type ProductWriteRequest = {
  name: string
  price: number
  description?: string | null
  specifications?: SpecificationRequest[]
}

export type OrderStatus =
  | 'EM_CADASTRAMENTO'
  | 'AGUARDANDO_PAGAMENTO'
  | 'AGUARDANDO_RESTAURANTE'
  | 'PREPARANDO'
  | 'SAIU_PARA_ENTREGA'
  | 'ENTREGADOR_NO_LOCAL'
  | 'ENTREGUE'
  | 'CANCELADO'

export type OrderPaymentStatus = 'PAGO' | 'CANCELADO'

export type OrderItemRequest = {
  productId: string
  specOptionIds?: string[]
}

export type SpecOptionSnapshot = {
  id: string
  name: string
  description?: string | null
  extraPrice: number
}

export type ProductSnapshot = {
  id: string
  name: string
  price: number
  description?: string | null
  specOptionSnapshots: SpecOptionSnapshot[]
}

export type Order = {
  id: string
  status: OrderStatus
  paymentStatus: OrderPaymentStatus | null
  score?: number | null
  restaurantId: string
  restaurantOwnerId: string
  clientId: string
  productSnapshots: ProductSnapshot[]
}

export type PaymentStatus = 'AGUARDANDO' | 'PAGO' | 'CANCELADO'

export type Payment = {
  id: string
  orderId: string
  userId: string
  status: PaymentStatus
  price: number
  stripeId?: string | null
}

export type CartItem = {
  key: string
  productId: string
  productName: string
  unitPrice: number
  restaurantId: string
  restaurantName: string
  specOptionIds: string[]
  specLabels: string[]
  quantity: number
}
