import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFish, defaultValue } from 'app/shared/model/fish.model';

export const ACTION_TYPES = {
  FETCH_FISH_LIST: 'fish/FETCH_FISH_LIST',
  FETCH_FISH: 'fish/FETCH_FISH',
  CREATE_FISH: 'fish/CREATE_FISH',
  UPDATE_FISH: 'fish/UPDATE_FISH',
  DELETE_FISH: 'fish/DELETE_FISH',
  RESET: 'fish/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFish>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type FishState = Readonly<typeof initialState>;

// Reducer

export default (state: FishState = initialState, action): FishState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FISH_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FISH):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_FISH):
    case REQUEST(ACTION_TYPES.UPDATE_FISH):
    case REQUEST(ACTION_TYPES.DELETE_FISH):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_FISH_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FISH):
    case FAILURE(ACTION_TYPES.CREATE_FISH):
    case FAILURE(ACTION_TYPES.UPDATE_FISH):
    case FAILURE(ACTION_TYPES.DELETE_FISH):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FISH_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_FISH):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_FISH):
    case SUCCESS(ACTION_TYPES.UPDATE_FISH):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_FISH):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/fish';

// Actions

export const getEntities: ICrudGetAllAction<IFish> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FISH_LIST,
  payload: axios.get<IFish>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IFish> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FISH,
    payload: axios.get<IFish>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IFish> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FISH,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFish> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FISH,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFish> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FISH,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
