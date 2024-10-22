import type { AddressCreationRequest } from "@/apiTypes";
import httpClient from "./httpClient";

const AddressApi = {
    getAddresses: async (username: string) => {
        return await httpClient.get("/user/address/get?username=" + username);
    },

    addAddress: async (creationAddress: AddressCreationRequest) => {
        return await httpClient.post("/user/address/add" , creationAddress);
    },
    deleteAddress: async (id: string) => {
        return await httpClient.delete("/user/address/delete/" + id);
    },
    setDefault: async (id: string) => {
        return await httpClient.put("/user/address/set-default/" + id);
    }
};

export default AddressApi