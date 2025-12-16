import { regularExps } from "../../config/regular-exp.js";

export class CreateUserDto {
  private constructor(
    public name: string,
    public email: string,
    public password: string
  ) {}

  static create( object: {[key:string]:any} ):[string | undefined, CreateUserDto | undefined] {
    const { name, email, password } = object;

    if(!name) return ['Missing Name', undefined];
    if(!email) return ['Missing Email', undefined];

    if(!regularExps.email.test(email)) return ['Email is not valid', undefined];
    if(!password) return ['Missing Password', undefined];
    if(password.length < 6) return ['Password must be at least 6 characters', undefined];

    return [undefined, new CreateUserDto(name, email, password)];
  }
}