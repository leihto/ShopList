package pl.wojciechpitek.shoplist.dtos;

import java.util.Comparator;

public class ListElementDto {

    public enum StateTypes {
        DEFAULT(0), CHECKED(1), DELETED(2);

        Integer type;
        StateTypes(Integer type) {
            this.type = type;
        }

        public Integer getType() {
            return type;
        }
    }

    private Long id;
    private String name;
    private StateTypes state = StateTypes.DEFAULT;
    private Boolean isImportant = Boolean.FALSE;

    public ListElementDto() {
    }

    public ListElementDto(String name, StateTypes state) {
        this.name = name;
        this.state = state;
    }

    public ListElementDto(String name, StateTypes state, Boolean isImportant) {
        this.name = name;
        this.state = state;
        this.isImportant = isImportant;
    }

    public ListElementDto(Long id, String name, StateTypes state, Boolean isImportant) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.isImportant = isImportant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public StateTypes getState() {
        return state;
    }

    public void setState(StateTypes state) {
        this.state = state;
    }

    public Boolean getImportant() {
        return isImportant;
    }

    public void setImportant(Boolean important) {
        isImportant = important;
    }

    public static StateTypes getStateByInt(Integer value) {
        for(StateTypes state : StateTypes.values()) {
            if(state.getType().equals(value)) {
                return state;
            }
        }
        return StateTypes.DEFAULT;
    }

    public static Boolean getIsImportant(Integer value) {
        return value > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Comparator<ListElementDto> sortItems = new Comparator<ListElementDto>() {
        @Override
        public int compare(ListElementDto element1, ListElementDto element2) {
            Boolean isDeleted = element1.getState().equals(StateTypes.DELETED);
            int compareStateDeleted = isDeleted.compareTo(element2.getState().equals(StateTypes.DELETED));
            if (compareStateDeleted == 0) {
                Boolean isChecked = element1.getState().equals(StateTypes.CHECKED);
                int compareIsChecked = isChecked.compareTo(element2.getState().equals(StateTypes.CHECKED));
                if(compareIsChecked == 0) {
                    return element2.getImportant().compareTo(element1.getImportant());
                }
                return compareIsChecked;
            }
            return compareStateDeleted;
        }
    };
}
