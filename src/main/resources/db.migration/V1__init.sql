--
-- PostgreSQL database dump
--

-- Dumped from database version 14.12
-- Dumped by pg_dump version 14.12

-- Started on 2025-02-05 01:00:26 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER DATABASE "fullstack_ecommerce" OWNER TO postgres;

ALTER SCHEMA public OWNER TO postgres;

COMMENT ON SCHEMA public IS 'standard public schema';

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 210 (class 1259 OID 18267)
-- Name: activation_codes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.activation_codes (
    id bigint NOT NULL,
    code character varying(255),
    created_at timestamp(6) without time zone,
    expires_at timestamp(6) without time zone,
    validated_at timestamp(6) without time zone,
    user_id bigint NOT NULL
);


ALTER TABLE public.activation_codes OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 18266)
-- Name: activation_codes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.activation_codes ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.activation_codes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 212 (class 1259 OID 18273)
-- Name: addresses; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.addresses (
    id bigint NOT NULL,
    bairro character varying(255),
    cep character varying(255),
    cidade character varying(255),
    estado character varying(255),
    logradouro character varying(255),
    numero character varying(255),
    user_id bigint NOT NULL
);


ALTER TABLE public.addresses OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 18272)
-- Name: addresses_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.addresses ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.addresses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 213 (class 1259 OID 18280)
-- Name: boleto_payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.boleto_payments (
    expires_at date,
    paid_at date,
    id bigint NOT NULL
);


ALTER TABLE public.boleto_payments OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 18286)
-- Name: brands; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.brands (
    id bigint NOT NULL,
    image text,
    name character varying(255)
);


ALTER TABLE public.brands OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 18285)
-- Name: brands_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.brands ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.brands_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 217 (class 1259 OID 18294)
-- Name: carts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.carts (
    id bigint NOT NULL
);


ALTER TABLE public.carts OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 18293)
-- Name: carts_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.carts ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.carts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 219 (class 1259 OID 18300)
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
    id bigint NOT NULL,
    image text,
    name character varying(255)
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 18299)
-- Name: categories_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.categories ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.categories_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 220 (class 1259 OID 18307)
-- Name: credit_card_payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.credit_card_payments (
    installments_quantity integer,
    id bigint NOT NULL,
    credit_card_id bigint
);


ALTER TABLE public.credit_card_payments OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 18313)
-- Name: credit_cards; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.credit_cards (
    id bigint NOT NULL,
    brand character varying(255),
    card_number character varying(255),
    cvv character varying(255),
    expiration_date date,
    holder_name character varying(255),
    user_id bigint NOT NULL,
    CONSTRAINT credit_cards_brand_check CHECK (((brand)::text = ANY ((ARRAY['VISA'::character varying, 'MASTERCARD'::character varying])::text[])))
);


ALTER TABLE public.credit_cards OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 18312)
-- Name: credit_cards_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.credit_cards ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.credit_cards_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 248 (class 1259 OID 18592)
-- Name: discount_coupons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discount_coupons (
    id bigint NOT NULL,
    available_days integer,
    code character varying(255),
    description character varying(255),
    discount_percentage numeric(38,2)
);


ALTER TABLE public.discount_coupons OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 18591)
-- Name: discount_coupons_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.discount_coupons ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.discount_coupons_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 224 (class 1259 OID 18322)
-- Name: orders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.orders (
    id bigint NOT NULL,
    moment timestamp(6) without time zone,
    order_status character varying(255),
    address_id bigint,
    payment_id bigint,
    user_id bigint NOT NULL,
    discount_coupon_id bigint,
    CONSTRAINT orders_order_status_check CHECK (((order_status)::text = ANY ((ARRAY['WAITING_PAYMENT'::character varying, 'PAID'::character varying, 'SHIPPED'::character varying, 'DELIVERED'::character varying, 'CANCELED'::character varying])::text[])))
);


ALTER TABLE public.orders OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 18321)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.orders ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.orders_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 226 (class 1259 OID 18329)
-- Name: password_recovers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.password_recovers (
    id bigint NOT NULL,
    code character varying(255),
    created_at timestamp(6) without time zone,
    expires_at timestamp(6) without time zone,
    validated_at timestamp(6) without time zone,
    user_id bigint NOT NULL
);


ALTER TABLE public.password_recovers OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 18328)
-- Name: password_recovers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.password_recovers ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.password_recovers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 227 (class 1259 OID 18334)
-- Name: payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payments (
    id bigint NOT NULL,
    payment_status character varying(255),
    payment_type character varying(255),
    total_value numeric(38,2),
    order_id bigint,
    CONSTRAINT payments_payment_status_check CHECK (((payment_status)::text = ANY ((ARRAY['PENDING'::character varying, 'PAID'::character varying, 'CANCELED'::character varying])::text[]))),
    CONSTRAINT payments_payment_type_check CHECK (((payment_type)::text = ANY ((ARRAY['CARTAO'::character varying, 'BOLETO'::character varying, 'PIX'::character varying])::text[])))
);


ALTER TABLE public.payments OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 18419)
-- Name: payments_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.payments_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.payments_seq OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 18343)
-- Name: pix_payments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.pix_payments (
    paid_at timestamp(6) without time zone,
    id bigint NOT NULL
);


ALTER TABLE public.pix_payments OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 18348)
-- Name: product_cart_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_cart_items (
    price numeric(38,2),
    quantity integer,
    product_id bigint NOT NULL,
    cart_id bigint NOT NULL
);


ALTER TABLE public.product_cart_items OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 18353)
-- Name: product_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_category (
    product_id bigint NOT NULL,
    category_id bigint NOT NULL
);


ALTER TABLE public.product_category OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 18357)
-- Name: product_images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_images (
    id bigint NOT NULL,
    image text,
    product_id bigint
);


ALTER TABLE public.product_images OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 18356)
-- Name: product_images_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.product_images ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.product_images_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 233 (class 1259 OID 18364)
-- Name: product_order_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_order_items (
    price numeric(38,2),
    quantity integer,
    product_id bigint NOT NULL,
    order_id bigint NOT NULL
);


ALTER TABLE public.product_order_items OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 18370)
-- Name: product_ratings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_ratings (
    id bigint NOT NULL,
    description character varying(255),
    rating double precision,
    product_id bigint,
    user_id bigint
);


ALTER TABLE public.product_ratings OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 18369)
-- Name: product_ratings_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.product_ratings ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.product_ratings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 236 (class 1259 OID 18375)
-- Name: product_wish_list_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product_wish_list_items (
    wish_list_id bigint NOT NULL,
    product_id bigint NOT NULL
);


ALTER TABLE public.product_wish_list_items OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 18379)
-- Name: products; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.products (
    id bigint NOT NULL,
    description text NOT NULL,
    is_in_offer boolean,
    name character varying(255) NOT NULL,
    offer_discount_percentage numeric(38,2),
    offer_expiration_date_time timestamp(6) without time zone,
    price numeric(38,2) NOT NULL,
    stock_quantity integer,
    views integer,
    brand_id bigint
);


ALTER TABLE public.products OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 18378)
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.products ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 240 (class 1259 OID 18387)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id bigint NOT NULL,
    authority character varying(255)
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 18386)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.roles ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 249 (class 1259 OID 18599)
-- Name: user_discount_coupon; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_discount_coupon (
    expires_at timestamp(6) without time zone,
    is_used boolean,
    used_at timestamp(6) without time zone,
    user_id bigint NOT NULL,
    discount_coupon_id bigint NOT NULL
);


ALTER TABLE public.user_discount_coupon OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 18392)
-- Name: user_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.user_role OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 18398)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint NOT NULL,
    activated boolean NOT NULL,
    birth_date date NOT NULL,
    cpf character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    full_name character varying(255) NOT NULL,
    password character varying(255) NOT NULL,
    cart_id bigint,
    wish_list_id bigint
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 18397)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.users ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 245 (class 1259 OID 18406)
-- Name: wish_lists; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.wish_lists (
    id bigint NOT NULL
);


ALTER TABLE public.wish_lists OWNER TO postgres;

--
-- TOC entry 244 (class 1259 OID 18405)
-- Name: wish_lists_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.wish_lists ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.wish_lists_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

--
-- TOC entry 3619 (class 0 OID 0)
-- Dependencies: 209
-- Name: activation_codes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.activation_codes_id_seq', 4, true);


--
-- TOC entry 3620 (class 0 OID 0)
-- Dependencies: 211
-- Name: addresses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.addresses_id_seq', 6, true);


--
-- TOC entry 3621 (class 0 OID 0)
-- Dependencies: 214
-- Name: brands_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.brands_id_seq', 8, true);


--
-- TOC entry 3622 (class 0 OID 0)
-- Dependencies: 216
-- Name: carts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.carts_id_seq', 8, true);


--
-- TOC entry 3623 (class 0 OID 0)
-- Dependencies: 218
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.categories_id_seq', 7, true);


--
-- TOC entry 3624 (class 0 OID 0)
-- Dependencies: 221
-- Name: credit_cards_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.credit_cards_id_seq', 6, true);


--
-- TOC entry 3625 (class 0 OID 0)
-- Dependencies: 247
-- Name: discount_coupons_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discount_coupons_id_seq', 2, true);


--
-- TOC entry 3626 (class 0 OID 0)
-- Dependencies: 223
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.orders_id_seq', 67, true);


--
-- TOC entry 3627 (class 0 OID 0)
-- Dependencies: 225
-- Name: password_recovers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.password_recovers_id_seq', 7, true);


--
-- TOC entry 3628 (class 0 OID 0)
-- Dependencies: 246
-- Name: payments_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.payments_seq', 901, true);


--
-- TOC entry 3629 (class 0 OID 0)
-- Dependencies: 231
-- Name: product_images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_images_id_seq', 154, true);


--
-- TOC entry 3630 (class 0 OID 0)
-- Dependencies: 234
-- Name: product_ratings_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_ratings_id_seq', 93, true);


--
-- TOC entry 3631 (class 0 OID 0)
-- Dependencies: 237
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.products_id_seq', 42, true);


--
-- TOC entry 3632 (class 0 OID 0)
-- Dependencies: 239
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 2, true);


--
-- TOC entry 3633 (class 0 OID 0)
-- Dependencies: 242
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 8, true);


--
-- TOC entry 3634 (class 0 OID 0)
-- Dependencies: 244
-- Name: wish_lists_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.wish_lists_id_seq', 8, true);




