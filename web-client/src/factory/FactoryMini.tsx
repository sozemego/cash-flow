import { IFactory } from "./index";
import React from "react";

export interface FactoryMiniProps {
    factory: IFactory
}

export function FactoryMini({factory}: FactoryMiniProps) {
    return (
        <div>
            HELLO AM FACTORY
        </div>
    )
}