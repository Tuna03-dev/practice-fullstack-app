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