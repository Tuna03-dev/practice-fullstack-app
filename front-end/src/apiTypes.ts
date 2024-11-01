export enum sliderNames {
  HOME = 'HOME_SLIDER'
}

export type ApiResponse<T> = {
  code: number
  data: T
  message: string
  timestamp: Date
}
export type CategoryResponse = {
  id: string
  name: string
  icon: string
}

export type ProductResponse = {
  id: string
  name: string
  price: bigint
  image: string
  priceWithDiscount: bigint
  averageRate: number
  categoryName: string
  soldQuantity: number
}

export type slidesResponse = {
  id: string
  imageUrl: string
  title: string
  description: string
  link: string
  orderIndex: number
  active: boolean
}

export type UpdateUserProfileRequest = {
  username: string
  email: string | null
  firstname?: string
  lastname?: string
  phone: string | null
  gender?: 'Male' | 'Female' | 'Other'
  birthDate?: Date
}

export type UserProfileResponse = {
  id: string
  username: string
  email: string
  firstname: string
  lastname: string
  role: string
  phone: string
  address: string
  imageUrl: string
  gender: string
  birthDate: Date
  status: boolean
  audit: {
    createdAt: Date
    updatedAt: Date
  }
}

export type ProductImageResponse = {
  id: string
  url: string
  description: string
}
export enum StatusShop {
  ACTIVE = 'ACTIVE',
  REJECT = 'REJECT',
  PENDING = 'PENDING',
  CANCELLED = 'CANCELLED'
}
export type ShopInformationType = {
  id: string
  name: string
  address: string
  createdAt: Date
  updatedAt: Date
  status: StatusShop
  averageRate: number
  numberOfRates: number
  requestDate: Date
  feePercentage: number
  imageUrl: string
  userId: string
  numberOfProducts: number
  joinedDate: string
  description: string
  descriptionImage: string
  categories: CategoryResponse[]
  favourite: boolean
}

export type ProductDetailResponse = {
  id: string
  name: string
  price: bigint
  image: string
  priceWithDiscount: bigint
  averageRate: number
  categoryId: string
  categoryName: string
  soldQuantity: number
  stockQuantity: number
  productImages: ProductImageResponse[]
  shopId: string
  description: string
  imageDescription?: string,
  brand?: string,
  origin?: string,
  material?: string,
  weight?: string,
}

export type ReviewResponse = {
  id: string
  userId: string
  productId: string
  material: string
  rating: number
  createdAt: string
  updatedAt: string
  comment: string
  colour: string
  trueDescription: string
  userName: string
  userAvatar: string
  likes: number
}

export type AddCartRequest = {
  productId: string
  quantity: number
  pricePerProduct: bigint
}

export type CartItemResponse = {
  id: string
  quantity: number
  pricePerProduct: bigint
  productId: string
  cartItemAmount: bigint
  productName: string
  productImage: string
}

export type CartResponse = {
  shopId: string
  shopName: string
  cartItemResponses: CartItemResponse[]
  totalAmount: bigint
}

export type AddressCreationRequest = {
  name: string
  phone: string
  province: string
  district: string
  ward: string
  street: string
}
export type Address = {
  id: string
  name: string
  phone: string
  province: string
  district: string
  ward: string
  street: string
  defaultAddress: boolean
}

export type ProductCreationRequest = {
  name: string
  price: number
  image: string
  categoryId: string
  stockQuantity: number
  listImages: string[]
  description: string
  imageDescription?: string,
  brand?: string,
  origin?: string,
  material?: string,
  weight?: string,
}

export type ShopUpdateRequest = {
  id: string
  name: string
  address: string
  description: string
  imageUrl: string
  descriptionImage: string
  categories: string[]
}

export interface ShippingMethod {
  id: string
  name: string
  cost: bigint
  provider: string
  estimatedDays: number
  logo: string
}

export type OrderCreationRequest = {
  addressId: string
  totalAmount: number
  totalAmountPaid: number
  shopOrderRequests: ShopOrderRequest[]
}

export type ShopOrderRequest = {
  shopId: string,
  shippingMethodId: string
  timeDelivery: number
  cartItemIds: string[]
  totalAmount: number
}

export type OrderResponseType = {
  id: string
  totalAmount: number
  status: string
  audit:{
    createdAt: Date
    updatedAt: Date
  }
  shopOrderResponses: ShopOrderResponse[]
}

export type ShopOrderResponse = {
  id: string
  shippingMethod: ShippingMethod
  totalAmount: number
  status: string
  shopInformationResponse: ShopInformationType
  estimatedDeliveryTime: Date
  orderItems: OrderItemResponse[]
  audit:{
    createdAt: Date
    updatedAt: Date
  }
  address: Address
}

export type OrderItemResponse = {
  id: string
  quantity: number
  pricePerUnit: number
  totalAmount: number
  productId: string
  productName: string
  productImageUrl: string
  totalAmountPaid: number
}