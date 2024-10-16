export enum sliderNames {
    HOME = "HOME_SLIDER",

}

export type CategoryResponse = {
    id: string,
    name: string,
    icon: string,

}

export type ProductResponse = {
    id: string,
    name: string,
    price: bigint,
    image: string,
    priceWithDiscount: bigint,
    averageRate: number,
    categoryName: string
    soldQuantity: number
}

export type slidesResponse = {
    id : string,
    imageUrl: string,
    title: string,
    description: string,
    link: string,
    orderIndex: number,
    active: boolean

} 


export type UserProfileResponse = {
    id: string,
    username: string,
    email: string,
    firstname: string,
    lastname: string,
    role: string
    phone: string,
    address: string,
    imageUrl: string,
    gender: boolean,
    birthDate: Date,
    status: boolean,

}


export type ProductImageResponse = {
    id: string,
    url: string,
    description: string
}
enum StatusShop{
    ACTIVE = "ACTIVE",
    REJECT = "REJECT",
    PENDING = "PENDING",
    CANCELLED = "CANCELLED",
}
export type ShopInformationType = {
    id: string,
    name: string,
    address: string,
    createdAt: Date,
    updatedAt: Date,
    status: StatusShop,
    averageRate: number,
    numberOfRates: number,
    requestDate : Date,
    feePercentage: number,
    imageUrl: string,
    userId: string,
    numberOfProducts: number,
    joinedDate: string,
    description: string,
    descriptionImage: string,

}
export type ProductDetailResponse = {
    id: string,
    name: string,
    price: bigint,
    image: string,
    priceWithDiscount: bigint,
    averageRate: number,
    categoryName: string,
    soldQuantity: number,
    stockQuantity: number,
    productImages: ProductImageResponse[],
    shopId: string,
 
}

export type ReviewResponse = {
    id: string,
    userId: string,
    productId: string,
    material: string,
    rating: number,
    createdAt: string,
    updatedAt: string,
    comment: string,
    colour: string,
    trueDescription: string,
    userName: string,
    userAvatar: string
    likes: number
}

export type AddCartRequest = {
    productId: string,
    quantity: number,
    pricePerProduct: bigint
}

export type CartItemResponse = {
    id: string,
    quantity: number,
    pricePerProduct: bigint,
    productId: string,
    cartItemAmount: bigint,
    productName: string,
    productImage: string
}