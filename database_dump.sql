--
-- PostgreSQL database dump
--

\restrict V646OctJUCZpEz8zIKRqmex2ZddCdBYXqG6Vv2O6vTeYpJ4e4zZhqsieWALZe6k

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-09-25 10:59:39

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 5026 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 220 (class 1259 OID 25413)
-- Name: accounts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.accounts (
    user_id integer NOT NULL,
    balance numeric(15,2) DEFAULT 0.00 NOT NULL
);


ALTER TABLE public.accounts OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 25412)
-- Name: accounts_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.accounts_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.accounts_user_id_seq OWNER TO postgres;

--
-- TOC entry 5027 (class 0 OID 0)
-- Dependencies: 219
-- Name: accounts_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.accounts_user_id_seq OWNED BY public.accounts.user_id;


--
-- TOC entry 222 (class 1259 OID 25423)
-- Name: operations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.operations (
    operation_id integer NOT NULL,
    user_id integer NOT NULL,
    type integer NOT NULL,
    amount integer,
    operation_date timestamp without time zone NOT NULL,
    target_user_id integer
);


ALTER TABLE public.operations OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 25422)
-- Name: operations_operation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.operations_operation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.operations_operation_id_seq OWNER TO postgres;

--
-- TOC entry 5028 (class 0 OID 0)
-- Dependencies: 221
-- Name: operations_operation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.operations_operation_id_seq OWNED BY public.operations.operation_id;


--
-- TOC entry 4861 (class 2604 OID 25416)
-- Name: accounts user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts ALTER COLUMN user_id SET DEFAULT nextval('public.accounts_user_id_seq'::regclass);


--
-- TOC entry 4863 (class 2604 OID 25426)
-- Name: operations operation_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.operations ALTER COLUMN operation_id SET DEFAULT nextval('public.operations_operation_id_seq'::regclass);


--
-- TOC entry 5018 (class 0 OID 25413)
-- Dependencies: 220
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.accounts (user_id, balance) FROM stdin;
2	1200.00
1	150.00
\.


--
-- TOC entry 5020 (class 0 OID 25423)
-- Dependencies: 222
-- Data for Name: operations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.operations (operation_id, user_id, type, amount, operation_date, target_user_id) FROM stdin;
1	1	1	100	2026-09-21 21:08:32.278686	\N
2	1	2	100	2026-09-22 19:28:19.377557	\N
6	1	3	50	2026-09-25 07:28:26.873721	2
7	1	3	50	2026-09-25 07:35:38.913704	2
8	1	3	50	2026-09-25 07:35:51.748716	2
9	1	3	50	2026-09-25 07:37:16.831552	2
10	1	1	100	2026-09-25 09:37:57.182808	\N
11	1	2	100	2026-09-25 09:41:46.288906	\N
12	1	1	100	2026-09-25 09:41:46.426658	\N
13	1	2	100	2026-09-25 09:42:51.65192	\N
14	1	1	100	2026-09-25 09:42:51.840059	\N
15	1	2	100	2026-09-25 09:47:14.778941	\N
16	1	1	100	2026-09-25 09:47:14.90324	\N
17	1	3	100	2026-09-25 09:53:35.471308	2
18	1	2	100	2026-09-25 09:53:35.598781	\N
19	1	1	100	2026-09-25 09:53:35.719747	\N
20	1	3	100	2026-09-25 09:55:46.181876	2
21	1	1	100	2026-09-25 09:55:46.618659	\N
22	1	3	100	2026-09-25 10:03:26.620957	2
23	1	1	100	2026-09-25 10:03:26.864339	\N
24	1	2	100	2026-09-25 10:04:30.137587	\N
25	1	1	100	2026-09-25 10:04:30.395595	\N
26	1	2	100	2026-09-25 10:05:36.874277	\N
27	1	1	100	2026-09-25 10:05:37.29813	\N
28	1	2	100	2026-09-25 10:06:36.789652	\N
29	1	1	100	2026-09-25 10:06:37.09874	\N
30	1	2	100	2026-09-25 10:07:32.64834	\N
31	1	1	100	2026-09-25 10:07:33.051447	\N
32	1	2	100	2026-09-25 10:08:28.029484	\N
33	1	1	100	2026-09-25 10:08:28.431187	\N
34	1	2	100	2026-09-25 10:09:11.394273	\N
35	1	1	100	2026-09-25 10:09:11.505	\N
36	1	3	100	2026-09-25 10:09:11.587738	2
37	1	1	100	2026-09-25 10:09:11.780732	\N
38	1	1	100	2026-09-25 10:34:43.383268	\N
39	1	1	100	2026-09-25 10:36:28.471379	\N
40	1	2	100	2026-09-25 10:36:28.66814	\N
41	1	1	100	2026-09-25 10:37:16.838113	\N
42	1	2	100	2026-09-25 10:37:16.934683	\N
43	1	1	100	2026-09-25 10:39:45.735019	\N
44	1	2	100	2026-09-25 10:39:45.893315	\N
45	1	3	100	2026-09-25 10:40:35.186003	2
46	1	1	100	2026-09-25 10:40:35.406283	\N
47	1	2	100	2026-09-25 10:40:35.554201	\N
\.


--
-- TOC entry 5029 (class 0 OID 0)
-- Dependencies: 219
-- Name: accounts_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.accounts_user_id_seq', 1, true);


--
-- TOC entry 5030 (class 0 OID 0)
-- Dependencies: 221
-- Name: operations_operation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.operations_operation_id_seq', 47, true);


--
-- TOC entry 4865 (class 2606 OID 25421)
-- Name: accounts accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (user_id);


--
-- TOC entry 4867 (class 2606 OID 25432)
-- Name: operations operations_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.operations
    ADD CONSTRAINT operations_pk PRIMARY KEY (operation_id);


--
-- TOC entry 4868 (class 2606 OID 25438)
-- Name: operations fk_target_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.operations
    ADD CONSTRAINT fk_target_user FOREIGN KEY (target_user_id) REFERENCES public.accounts(user_id);


--
-- TOC entry 4869 (class 2606 OID 25433)
-- Name: operations operations_accounts_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.operations
    ADD CONSTRAINT operations_accounts_fk FOREIGN KEY (user_id) REFERENCES public.accounts(user_id);


-- Completed on 2026-09-25 10:59:39

--
-- PostgreSQL database dump complete
--

\unrestrict V646OctJUCZpEz8zIKRqmex2ZddCdBYXqG6Vv2O6vTeYpJ4e4zZhqsieWALZe6k

