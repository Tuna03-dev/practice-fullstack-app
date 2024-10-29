import type { sliderNames, ApiResponse } from "../apiTypes";
import httpClient from "./httpClient";

const SliderApi = {
    getAllSlidesBySliderName: async (sliderName: sliderNames): Promise<ApiResponse<any>> => {
        const res = await httpClient.get<ApiResponse<any>>(`/sliders/get-slides/${sliderName}`);
        return res.data;
    }
};

export default SliderApi;
