import React from "react";
import { PageHeader } from "antd";

export function Header() {
	return (
		<PageHeader
			style={{
				border: '1px solid rgb(235, 237, 240)',
			}}
			title="Log aggregator"
		/>
	)
}