import React from "react";
import { Section, SectionName } from "./index";
import CheckableTag from "antd/lib/tag/CheckableTag";
import { useGetSelectedSections } from "./selectors";
import { useDispatch } from "react-redux";
import { sectionDisplaySelected } from "./actions";

const sectionStrings: SectionName = {
  [Section.GAME_EVENT]: "Game events",
  [Section.CITY]: "Cities",
  [Section.FACTORY]: "Factories",
  [Section.TRUCK]: "Trucks"
};

export function SectionSelector() {
  const sectionsSelected = useGetSelectedSections();
  const dispatch = useDispatch();

  return (
    <div>
      {Object.keys(sectionsSelected).map(section => {
        return (
          <CheckableTag
            checked={sectionsSelected[section as Section]}
            key={section}
            onChange={checked => {
              const nextSectionsSelected = {...sectionsSelected, [section]: checked};
              dispatch(sectionDisplaySelected(nextSectionsSelected));
            }}
          >
            {sectionStrings[section as Section]}
          </CheckableTag>
        );
      })}
    </div>
  );
}
