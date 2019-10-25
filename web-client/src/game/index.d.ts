interface ResourceDatas {
    [key: string]: ResourceData;
}

interface ResourceData {
    count: number;
    averagePrice: number;
    saturation: number;
    factories: number;
}
